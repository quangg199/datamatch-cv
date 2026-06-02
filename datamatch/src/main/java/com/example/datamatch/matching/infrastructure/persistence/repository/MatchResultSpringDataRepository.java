package com.example.datamatch.matching.infrastructure.persistence.repository;

import com.example.datamatch.matching.infrastructure.persistence.entity.MatchResultJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MatchResultSpringDataRepository extends JpaRepository<MatchResultJpaEntity, UUID> {
}
