package com.example.datamatch.job.presentation.dto;

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
public class JobResponseDTO {

    private UUID id;
    private String title;
    private String companyName;
    private List<String> requiredSkills;
}
