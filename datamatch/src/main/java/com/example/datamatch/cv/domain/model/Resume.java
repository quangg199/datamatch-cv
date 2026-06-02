package com.example.datamatch.cv.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Resume {

    private final UUID id;
    private final String fileName;
    private final String storagePath;
    private String rawText;
    private List<String> skills;
    private CvStatus status;

    public Resume(String fileName, String storagePath) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.status = CvStatus.INIT;
    }

    public void updateRawText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Raw text cannot be blank.");
        }
        this.rawText = text;
        this.status = CvStatus.PARSED;
    }

    public void convertToSkills(List<String> extractedSkills) {
        if (this.status != CvStatus.PARSED) {
            throw new IllegalStateException("Resume must be in PARSED state to extract skills. Current state: " + this.status);
        }
        
        if (extractedSkills == null) {
            this.skills = Collections.emptyList();
        } else {
            this.skills = extractedSkills.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        
        this.status = CvStatus.EXTRACTED;
    }

    public void markAsFailed() {
        this.status = CvStatus.FAILED;
    }

    public UUID getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getRawText() {
        return rawText;
    }

    public List<String> getSkills() {
        if (skills == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(skills);
    }

    public CvStatus getStatus() {
        return status;
    }
}
