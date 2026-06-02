package com.example.datamatch.matching.infrastructure.client;

import com.example.datamatch.job.application.ports.JobDescriptionRepositoryPort;
import com.example.datamatch.job.domain.model.JobDescription;
import com.example.datamatch.matching.application.ports.FetchJdSkillsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobModuleAdapter implements FetchJdSkillsPort {

    private final JobDescriptionRepositoryPort jobDescriptionRepositoryPort;

    @Override
    public List<String> fetchSkills(UUID jobId) {
        return jobDescriptionRepositoryPort.findById(jobId)
                .map(JobDescription::getRequiredSkills)
                .orElseThrow(() -> new IllegalArgumentException("Job Description not found: " + jobId));
    }
}
