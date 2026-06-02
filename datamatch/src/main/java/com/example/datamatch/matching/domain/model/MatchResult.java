package com.example.datamatch.matching.domain.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class MatchResult {

    private final UUID id;
    private final UUID resumeId;
    private final UUID jobDescriptionId;
    private final List<String> cvSkills;
    private final List<String> jdSkills;
    private double score;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public MatchResult(UUID resumeId, UUID jobDescriptionId, List<String> cvSkills, List<String> jdSkills) {
        this.id = UUID.randomUUID();
        this.resumeId = resumeId;
        this.jobDescriptionId = jobDescriptionId;
        this.cvSkills = normalize(cvSkills);
        this.jdSkills = normalize(jdSkills);
        executeMatching();
    }

    private List<String> normalize(List<String> skills) {
        if (skills == null) {
            return Collections.emptyList();
        }
        return skills.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private void executeMatching() {
        if (this.jdSkills.isEmpty()) {
            this.score = 0.0;
            this.matchedSkills = Collections.emptyList();
            this.missingSkills = Collections.emptyList();
            return;
        }

        this.matchedSkills = this.cvSkills.stream()
                .filter(this.jdSkills::contains)
                .collect(Collectors.toList());

        this.missingSkills = this.jdSkills.stream()
                .filter(skill -> !this.cvSkills.contains(skill))
                .collect(Collectors.toList());

        double calculatedScore = ((double) this.matchedSkills.size() / this.jdSkills.size()) * 100.0;
        this.score = Math.round(calculatedScore * 100.0) / 100.0;
    }

    public UUID getId() {
        return id;
    }

    public UUID getResumeId() {
        return resumeId;
    }

    public UUID getJobDescriptionId() {
        return jobDescriptionId;
    }

    public List<String> getCvSkills() {
        return Collections.unmodifiableList(cvSkills);
    }

    public List<String> getJdSkills() {
        return Collections.unmodifiableList(jdSkills);
    }

    public double getScore() {
        return score;
    }

    public List<String> getMatchedSkills() {
        return Collections.unmodifiableList(matchedSkills);
    }

    public List<String> getMissingSkills() {
        return Collections.unmodifiableList(missingSkills);
    }
}
