package com.example.datamatch.cv.application.ports;

import java.util.List;

public interface SkillExtractorPort {
    List<String> extractSkills(String rawText);
}
