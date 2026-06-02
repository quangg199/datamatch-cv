package com.example.datamatch.matching.presentation.dto;

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
public class MatchResponseDTO {

    private UUID matchResultId;
    private double score;
    private List<String> matchedSkills;
    private List<String> missingSkills;
}
