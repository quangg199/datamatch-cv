package com.example.datamatch.job.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobCreateRequestDTO {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Company name must not be blank")
    private String companyName;

    @NotBlank(message = "Raw description must not be blank")
    private String rawDescription;
}
