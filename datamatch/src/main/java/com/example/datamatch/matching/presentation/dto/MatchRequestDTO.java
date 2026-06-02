package com.example.datamatch.matching.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestDTO {

    @NotNull(message = "Resume ID must not be null")
    private UUID resumeId;

    @NotNull(message = "Job Description ID must not be null")
    private UUID jobDescriptionId;

    @NotEmpty(message = "CV skills list must not be empty")
    private List<String> cvSkills;

    @NotEmpty(message = "JD skills list must not be empty")
    private List<String> jdSkills;
}
