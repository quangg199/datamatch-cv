package com.example.datamatch.matching.application.ports;

import java.util.List;
import java.util.UUID;

public interface FetchCvSkillsPort {
    List<String> fetchSkills(UUID resumeId);
}
