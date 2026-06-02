package com.example.datamatch.matching.infrastructure.client;

import com.example.datamatch.cv.application.ports.ResumeRepositoryPort;
import com.example.datamatch.cv.domain.model.Resume;
import com.example.datamatch.matching.application.ports.FetchCvSkillsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CvModuleAdapter implements FetchCvSkillsPort {

    private final ResumeRepositoryPort resumeRepositoryPort;

    @Override
    public List<String> fetchSkills(UUID resumeId) {
        return resumeRepositoryPort.findById(resumeId)
                .map(Resume::getSkills)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found: " + resumeId));
    }
}
