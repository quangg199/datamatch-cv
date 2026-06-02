package com.example.datamatch.cv.infrastructure.ai;

import com.example.datamatch.cv.application.ports.SkillExtractorPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpringAiGeminiAdapter implements SkillExtractorPort {

    private static final String SYSTEM_PROMPT = """
            You are an expert multilingual IT recruiter specializing in resume/CV skill extraction.
            Your task: Extract ALL professional technical skills, frameworks, tools, databases, \
            programming languages, methodologies, and soft skills from the provided text.
            
            CRITICAL RULES:
            1. The input text may be in ANY language (Vietnamese, English, Japanese, etc.). \
               You MUST correctly parse non-English sentence structures.
            2. Extract ALL skills regardless of proficiency level mentioned \
               (beginner, intermediate, advanced, familiar, experienced, etc.).
            3. Return skill names in their standard English/technical form \
               (e.g., "Spring Boot" not "framework Spring Boot").
            4. Return ONLY a raw JSON array of strings. No markdown, no explanation, no extra text.
            """;

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Override
    public List<String> extractSkills(String rawText) {
        log.info("[CV AI] Sending rawText to LLM, length: {}", rawText != null ? rawText.length() : 0);
        try {
            ChatClient chatClient = ChatClient.create(chatModel);

            String rawResponse = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(rawText)
                    .call()
                    .content();
            log.info("[CV AI] Raw LLM response: {}", rawResponse);

            if (rawResponse == null || rawResponse.isBlank()) {
                return Collections.emptyList();
            }

            // Dọn dẹp markdown
            String cleanedResponse = rawResponse.trim();
            if (cleanedResponse.startsWith("```json")) {
                cleanedResponse = cleanedResponse.substring(7);
            } else if (cleanedResponse.startsWith("```")) {
                cleanedResponse = cleanedResponse.substring(3);
            }
            if (cleanedResponse.endsWith("```")) {
                cleanedResponse = cleanedResponse.substring(0, cleanedResponse.length() - 3);
            }
            cleanedResponse = cleanedResponse.trim();

            List<String> skills = objectMapper.readValue(cleanedResponse, new TypeReference<List<String>>() {});

            log.info("[CV AI] Parsed {} skills: {}", skills.size(), skills);
            return skills;
        } catch (Exception e) {
            log.error("[CV AI] FAILED to extract skills: {}", e.getMessage(), e);
            throw new RuntimeException("AI extraction failed: " + e.getMessage(), e);
        }
    }
}


