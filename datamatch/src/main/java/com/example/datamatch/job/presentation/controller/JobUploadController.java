package com.example.datamatch.job.presentation.controller;

import com.example.datamatch.job.application.service.JobPdfUploadService;
import com.example.datamatch.job.domain.model.JobDescription;
import com.example.datamatch.job.presentation.dto.JobUploadResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobUploadController {

    private final JobPdfUploadService jobPdfUploadService;
    private static final String UPLOAD_DIR = "uploads/jd/";

    /**
     * Upload JD dưới dạng file PDF.
     * Sau khi upload thành công, gọi POST /api/v1/jobs/{id}/extract-skills để bóc tách kỹ năng.
     */
    @PostMapping("/upload")
    public ResponseEntity<JobUploadResponseDTO> uploadJd(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("companyName") String companyName) {

        if (file.isEmpty() || file.getOriginalFilename() == null
                || !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().build();
        }

        if (title == null || title.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (companyName == null || companyName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path destinationPath = uploadPath.resolve(uniqueFileName).normalize();
            file.transferTo(destinationPath);

            JobDescription jobDescription = jobPdfUploadService.uploadAndCreateJob(
                    title.trim(),
                    companyName.trim(),
                    destinationPath.toString()
            );

            JobUploadResponseDTO response = new JobUploadResponseDTO(
                    jobDescription.getId(),
                    jobDescription.getTitle(),
                    jobDescription.getCompanyName(),
                    file.getOriginalFilename(),
                    jobDescription.getRequiredSkills()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
