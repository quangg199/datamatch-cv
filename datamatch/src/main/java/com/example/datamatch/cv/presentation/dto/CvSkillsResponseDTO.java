package com.example.datamatch.cv.presentation.dto;

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
public class CvSkillsResponseDTO {

    private UUID id;
    private String status;
    private List<String> skills;
}
