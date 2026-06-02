package com.example.datamatch.job.infrastructure.ai;

import com.example.datamatch.job.application.ports.JobSkillExtractorPort;
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
public class JobSkillExtractorAdapter implements JobSkillExtractorPort {

    private static final String SYSTEM_PROMPT = """
            You are an expert multilingual technical recruiter specializing in IT skill extraction.
            Your task: Extract ALL technical skills, frameworks, platforms, programming languages, \
            databases, tools, methodologies, and soft skills from the job description.
            
            CRITICAL RULES:
            1. The input text may be in ANY language (Vietnamese, English, Japanese, etc.). \
               You MUST correctly parse non-English sentence structures.
            2. Extract BOTH mandatory/required skills AND nice-to-have/preferred/advantage skills. \
               Do NOT distinguish between them — include everything.
            3. Return skill names in their standard English/technical form \
               (e.g., "PostgreSQL" not "cơ sở dữ liệu PostgreSQL").
            4. Return ONLY a raw JSON array of strings. No markdown, no explanation, no extra text.
            """;

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    @Override
    public List<String> extractSkills(String rawDescription) {
        log.info("[Job AI] Sending rawDescription to LLM, length: {}", rawDescription != null ? rawDescription.length() : 0);
        try {
            ChatClient chatClient = ChatClient.create(chatModel);
            
            String rawResponse = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(rawDescription)
                    .call()
                    .content();
                    
            log.info("[Job AI] Raw LLM response: {}", rawResponse);

            if (rawResponse == null || rawResponse.isBlank()) {
                return Collections.emptyList();
            }

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

            log.info("[Job AI] Parsed {} skills: {}", skills.size(), skills);
            return skills;
        } catch (Exception e) {
            log.error("[Job AI] FAILED to extract skills: {}", e.getMessage(), e);
            throw new RuntimeException("AI extraction failed: " + e.getMessage(), e);
        }
    }
}

