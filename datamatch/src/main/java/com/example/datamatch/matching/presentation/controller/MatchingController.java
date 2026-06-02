package com.example.datamatch.matching.presentation.controller;

import com.example.datamatch.matching.application.service.MatchingApplicationService;
import com.example.datamatch.matching.domain.model.MatchResult;
import com.example.datamatch.matching.presentation.dto.AutoMatchRequestDTO;
import com.example.datamatch.matching.presentation.dto.MatchRequestDTO;
import com.example.datamatch.matching.presentation.dto.MatchResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingApplicationService matchingApplicationService;

    @PostMapping
    public ResponseEntity<MatchResponseDTO> processMatching(@Valid @RequestBody MatchRequestDTO request) {
        
        MatchResult matchResult = matchingApplicationService.executeMatching(
                request.getResumeId(),
                request.getJobDescriptionId(),
                request.getCvSkills(),
                request.getJdSkills()
        );

        MatchResponseDTO responseDTO = new MatchResponseDTO(
                matchResult.getId(),
                matchResult.getScore(),
                matchResult.getMatchedSkills(),
                matchResult.getMissingSkills()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/auto")
    public ResponseEntity<MatchResponseDTO> automateMatching(@Valid @RequestBody AutoMatchRequestDTO request) {
        MatchResult matchResult = matchingApplicationService.automateMatching(
                request.getResumeId(),
                request.getJobDescriptionId()
        );

        MatchResponseDTO responseDTO = new MatchResponseDTO(
                matchResult.getId(),
                matchResult.getScore(),
                matchResult.getMatchedSkills(),
                matchResult.getMissingSkills()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
