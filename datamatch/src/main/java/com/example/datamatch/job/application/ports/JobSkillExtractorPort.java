package com.example.datamatch.job.application.ports;

import java.util.List;

public interface JobSkillExtractorPort {
    List<String> extractSkills(String rawDescription);
}
