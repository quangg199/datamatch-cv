package com.example.datamatch.cv.infrastructure.persistence.repository;

import com.example.datamatch.cv.infrastructure.persistence.entity.ResumeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResumeSpringDataRepository extends JpaRepository<ResumeJpaEntity, UUID> {
}
