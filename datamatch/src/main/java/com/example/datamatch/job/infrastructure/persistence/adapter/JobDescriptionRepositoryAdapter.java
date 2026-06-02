package com.example.datamatch.job.infrastructure.persistence.adapter;

import com.example.datamatch.job.application.ports.JobDescriptionRepositoryPort;
import com.example.datamatch.job.domain.model.JobDescription;
import com.example.datamatch.job.infrastructure.persistence.entity.JobDescriptionJpaEntity;
import com.example.datamatch.job.infrastructure.persistence.repository.JobDescriptionSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JobDescriptionRepositoryAdapter implements JobDescriptionRepositoryPort {

    private final JobDescriptionSpringDataRepository repository;

    @Override
    public void save(JobDescription jobDescription) {
        JobDescriptionJpaEntity entity = new JobDescriptionJpaEntity(
                jobDescription.getId(),
                jobDescription.getTitle(),
                jobDescription.getCompanyName(),
                jobDescription.getRawDescription(),
                jobDescription.getRequiredSkills()
        );
        repository.save(entity);
    }

    @Override
    public Optional<JobDescription> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    private JobDescription toDomain(JobDescriptionJpaEntity entity) {
        JobDescription jobDescription = new JobDescription(
                entity.getTitle(), 
                entity.getCompanyName(), 
                entity.getRawDescription()
        );
        try {
            java.lang.reflect.Field idField = JobDescription.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(jobDescription, entity.getId());
            
            java.lang.reflect.Field skillsField = JobDescription.class.getDeclaredField("requiredSkills");
            skillsField.setAccessible(true);
            skillsField.set(jobDescription, entity.getRequiredSkills());
        } catch (Exception e) {
            throw new RuntimeException("Failed to map JobDescription entity to domain", e);
        }
        return jobDescription;
    }
}
