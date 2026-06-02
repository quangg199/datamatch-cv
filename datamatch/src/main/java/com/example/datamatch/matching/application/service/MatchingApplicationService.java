package com.example.datamatch.matching.application.service;

import com.example.datamatch.matching.application.ports.MatchResultRepositoryPort;
import com.example.datamatch.matching.application.ports.FetchCvSkillsPort;
import com.example.datamatch.matching.application.ports.FetchJdSkillsPort;
import com.example.datamatch.matching.domain.model.MatchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingApplicationService {

    private final MatchResultRepositoryPort matchResultRepositoryPort;
    private final FetchCvSkillsPort fetchCvSkillsPort;
    private final FetchJdSkillsPort fetchJdSkillsPort;

    public MatchResult executeMatching(UUID resumeId, UUID jobDescriptionId, List<String> cvSkills, List<String> jdSkills) {
        MatchResult matchResult = new MatchResult(resumeId, jobDescriptionId, cvSkills, jdSkills);
        matchResultRepositoryPort.save(matchResult);
        return matchResult;
    }

    public MatchResult automateMatching(UUID resumeId, UUID jobId) {
        List<String> cvSkills = fetchCvSkillsPort.fetchSkills(resumeId);
        List<String> jdSkills = fetchJdSkillsPort.fetchSkills(jobId);
        MatchResult matchResult = new MatchResult(resumeId, jobId, cvSkills, jdSkills);
        matchResultRepositoryPort.save(matchResult);
        return matchResult;
    }
}
