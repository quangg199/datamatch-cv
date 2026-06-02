package com.example.datamatch.cv.presentation.controller;

import com.example.datamatch.cv.application.service.CvApplicationService;
import com.example.datamatch.cv.domain.model.Resume;
import com.example.datamatch.cv.presentation.dto.CvSkillsResponseDTO;
import com.example.datamatch.cv.presentation.dto.CvUploadResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cvs")
@RequiredArgsConstructor
public class CvController {

    private final CvApplicationService cvApplicationService;
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<CvUploadResponseDTO> uploadCv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null || !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
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

            Resume resume = cvApplicationService.uploadAndParseCv(file.getOriginalFilename(), destinationPath.toString());

            CvUploadResponseDTO responseDTO = new CvUploadResponseDTO(
                    resume.getId(),
                    resume.getFileName(),
                    resume.getStatus().name()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/extract-skills")
    public ResponseEntity<CvSkillsResponseDTO> extractSkills(@PathVariable UUID id) {
        Resume resume = cvApplicationService.extractCvSkills(id);

        CvSkillsResponseDTO responseDTO = new CvSkillsResponseDTO(
                resume.getId(),
                resume.getStatus().name(),
                resume.getSkills()
        );

        return ResponseEntity.ok(responseDTO);
    }
}
