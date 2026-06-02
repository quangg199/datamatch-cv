package com.example.datamatch.job.infrastructure.persistence.repository;

import com.example.datamatch.job.infrastructure.persistence.entity.JobDescriptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobDescriptionSpringDataRepository extends JpaRepository<JobDescriptionJpaEntity, UUID> {
}
