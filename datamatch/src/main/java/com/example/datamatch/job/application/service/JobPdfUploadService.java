package com.example.datamatch.job.application.service;

import com.example.datamatch.job.application.ports.JdPdfParserPort;
import com.example.datamatch.job.application.ports.JobDescriptionRepositoryPort;
import com.example.datamatch.job.domain.model.JobDescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPdfUploadService {

    private final JdPdfParserPort jdPdfParserPort;
    private final JobDescriptionRepositoryPort jobDescriptionRepositoryPort;

    /**
     * Parse PDF file → extract raw text → tạo JobDescription → lưu DB.
     * Sau đó user gọi riêng POST /api/v1/jobs/{id}/extract-skills để bóc tách kỹ năng.
     */
    public JobDescription uploadAndCreateJob(String title, String companyName, String storagePath) {
        String rawText = jdPdfParserPort.parse(storagePath);
        log.info("[JD Upload] Parsed PDF, rawText length: {}", rawText != null ? rawText.length() : 0);

        if (rawText == null || rawText.trim().isEmpty()) {
            throw new IllegalStateException("Failed to extract text from JD PDF. The file may be image-based or empty.");
        }

        JobDescription jobDescription = new JobDescription(title, companyName, rawText);
        jobDescriptionRepositoryPort.save(jobDescription);

        log.info("[JD Upload] Created JobDescription ID: {}, title: '{}'", jobDescription.getId(), title);
        return jobDescription;
    }
}
