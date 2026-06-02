package com.example.datamatch.matching.infrastructure.persistence.adapter;

import com.example.datamatch.matching.application.ports.MatchResultRepositoryPort;
import com.example.datamatch.matching.domain.model.MatchResult;
import com.example.datamatch.matching.infrastructure.persistence.entity.MatchResultJpaEntity;
import com.example.datamatch.matching.infrastructure.persistence.repository.MatchResultSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchResultRepositoryAdapter implements MatchResultRepositoryPort {

    private final MatchResultSpringDataRepository repository;

    @Override
    public void save(MatchResult matchResult) {
        MatchResultJpaEntity entity = new MatchResultJpaEntity(
                matchResult.getId(),
                matchResult.getResumeId(),
                matchResult.getJobDescriptionId(),
                matchResult.getScore(),
                matchResult.getCvSkills(),
                matchResult.getJdSkills(),
                matchResult.getMatchedSkills(),
                matchResult.getMissingSkills()
        );
        repository.save(entity);
    }
}
