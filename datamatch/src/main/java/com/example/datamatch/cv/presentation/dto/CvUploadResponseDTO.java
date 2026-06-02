package com.example.datamatch.cv.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CvUploadResponseDTO {

    private UUID id;
    private String fileName;
    private String status;
}
