package com.example.datamatch.job.presentation.controller;

import com.example.datamatch.job.application.service.JobApplicationService;
import com.example.datamatch.job.domain.model.JobDescription;
import com.example.datamatch.job.presentation.dto.JobCreateRequestDTO;
import com.example.datamatch.job.presentation.dto.JobResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobCreateRequestDTO request) {
        JobDescription jobDescription = jobApplicationService.createJob(
                request.getTitle(),
                request.getCompanyName(),
                request.getRawDescription()
        );

        JobResponseDTO response = new JobResponseDTO(
                jobDescription.getId(),
                jobDescription.getTitle(),
                jobDescription.getCompanyName(),
                jobDescription.getRequiredSkills()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/extract-skills")
    public ResponseEntity<JobResponseDTO> extractSkills(@PathVariable UUID id) {
        JobDescription jobDescription = jobApplicationService.extractRequiredSkills(id);

        JobResponseDTO response = new JobResponseDTO(
                jobDescription.getId(),
                jobDescription.getTitle(),
                jobDescription.getCompanyName(),
                jobDescription.getRequiredSkills()
        );

        return ResponseEntity.ok(response);
    }
}
