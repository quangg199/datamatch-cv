package com.example.datamatch.job.application.service;

import com.example.datamatch.job.application.ports.JobDescriptionRepositoryPort;
import com.example.datamatch.job.application.ports.JobSkillExtractorPort;
import com.example.datamatch.job.domain.model.JobDescription;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobDescriptionRepositoryPort jobDescriptionRepositoryPort;
    private final JobSkillExtractorPort jobSkillExtractorPort;

    public JobDescription createJob(String title, String companyName, String rawDescription) {
        JobDescription jobDescription = new JobDescription(title, companyName, rawDescription);
        jobDescriptionRepositoryPort.save(jobDescription);
        return jobDescription;
    }

    public JobDescription extractRequiredSkills(UUID jobId) {
        JobDescription job = jobDescriptionRepositoryPort.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job Description not found for ID: " + jobId));
        
        List<String> skills = jobSkillExtractorPort.extractSkills(job.getRawDescription());
        job.assignRequiredSkills(skills);
        
        jobDescriptionRepositoryPort.save(job);
        return job;
    }
}
