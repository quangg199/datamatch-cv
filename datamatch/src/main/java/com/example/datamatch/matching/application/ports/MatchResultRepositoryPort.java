package com.example.datamatch.matching.application.ports;

import com.example.datamatch.matching.domain.model.MatchResult;

public interface MatchResultRepositoryPort {
    void save(MatchResult matchResult);
}
