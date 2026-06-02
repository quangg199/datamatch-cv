package com.example.datamatch.job.application.ports;

import com.example.datamatch.job.domain.model.JobDescription;
import java.util.Optional;
import java.util.UUID;

public interface JobDescriptionRepositoryPort {
    void save(JobDescription jobDescription);
    Optional<JobDescription> findById(UUID id);
}
