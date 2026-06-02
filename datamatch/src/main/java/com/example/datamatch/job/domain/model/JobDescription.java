package com.example.datamatch.job.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class JobDescription {

    private final UUID id;
    private final String title;
    private final String companyName;
    private final String rawDescription;
    private List<String> requiredSkills;

    public JobDescription(String title, String companyName, String rawDescription) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be blank");
        }
        if (rawDescription == null || rawDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Raw description cannot be blank");
        }

        this.id = UUID.randomUUID();
        this.title = title.trim();
        this.companyName = companyName.trim();
        this.rawDescription = rawDescription;
        this.requiredSkills = new ArrayList<>();
    }

    public void assignRequiredSkills(List<String> skills) {
        if (skills == null) {
            this.requiredSkills = new ArrayList<>();
            return;
        }

        this.requiredSkills = skills.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRawDescription() {
        return rawDescription;
    }

    public List<String> getRequiredSkills() {
        if (requiredSkills == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(requiredSkills);
    }
}
