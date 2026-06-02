package com.example.datamatch.cv.application.service;

import com.example.datamatch.cv.application.ports.PdfParserPort;
import com.example.datamatch.cv.application.ports.ResumeRepositoryPort;
import com.example.datamatch.cv.application.ports.SkillExtractorPort;
import com.example.datamatch.cv.domain.model.Resume;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CvApplicationService {

    private final ResumeRepositoryPort resumeRepositoryPort;
    private final PdfParserPort pdfParserPort;
    private final SkillExtractorPort skillExtractorPort;

    public Resume uploadAndParseCv(String fileName, String storagePath) {
        Resume resume = new Resume(fileName, storagePath);
        String rawText = pdfParserPort.parse(storagePath);
        log.info("[CV Upload] Parsed PDF '{}', rawText length: {}", fileName, rawText != null ? rawText.length() : 0);
        resume.updateRawText(rawText);
        resumeRepositoryPort.save(resume);
        return resume;
    }

    public Resume extractCvSkills(UUID cvId) {
        Resume resume = resumeRepositoryPort.findById(cvId)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found for ID: " + cvId));

        String rawText = resume.getRawText();
        log.info("[CV Extract] Resume ID: {}, status: {}, rawText is null: {}, rawText length: {}",
                cvId, resume.getStatus(), rawText == null, rawText != null ? rawText.length() : 0);

        if (rawText == null || rawText.trim().isEmpty()) {
            log.error("[CV Extract] ABORT — rawText is null or empty for Resume ID: {}. PDF was not parsed correctly.", cvId);
            throw new IllegalStateException("Cannot extract skills: rawText is empty for Resume ID: " + cvId);
        }

        log.info("[CV Extract] rawText preview (first 200 chars): {}", rawText.substring(0, Math.min(200, rawText.length())));

        List<String> extractedSkills = skillExtractorPort.extractSkills(rawText);
        log.info("[CV Extract] AI returned {} skills: {}", extractedSkills.size(), extractedSkills);

        resume.convertToSkills(extractedSkills);
        resumeRepositoryPort.save(resume);

        log.info("[CV Extract] Saved Resume ID: {}, final skills count: {}, status: {}",
                resume.getId(), resume.getSkills().size(), resume.getStatus());

        return resume;
    }
}

