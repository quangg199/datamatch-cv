package com.example.datamatch.cv.infrastructure.persistence.adapter;

import com.example.datamatch.cv.application.ports.ResumeRepositoryPort;
import com.example.datamatch.cv.domain.model.Resume;
import com.example.datamatch.cv.infrastructure.persistence.entity.ResumeJpaEntity;
import com.example.datamatch.cv.infrastructure.persistence.repository.ResumeSpringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ResumeRepositoryAdapter implements ResumeRepositoryPort {

    private final ResumeSpringDataRepository repository;

    @Override
    public void save(Resume resume) {
        ResumeJpaEntity entity = new ResumeJpaEntity(
                resume.getId(),
                resume.getFileName(),
                resume.getStoragePath(),
                resume.getRawText(),
                resume.getStatus(),
                resume.getSkills()
        );
        repository.save(entity);
    }

    @Override
    public Optional<Resume> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    private Resume toDomain(ResumeJpaEntity entity) {
        Resume resume = new Resume(entity.getFileName(), entity.getStoragePath());
        try {
            java.lang.reflect.Field idField = Resume.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(resume, entity.getId());

            java.lang.reflect.Field statusField = Resume.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(resume, entity.getStatus());

            java.lang.reflect.Field rawTextField = Resume.class.getDeclaredField("rawText");
            rawTextField.setAccessible(true);
            rawTextField.set(resume, entity.getRawText());

            java.lang.reflect.Field skillsField = Resume.class.getDeclaredField("skills");
            skillsField.setAccessible(true);
            skillsField.set(resume, entity.getSkills());
        } catch (Exception e) {
            throw new RuntimeException("Failed to map Resume entity to domain", e);
        }
        return resume;
    }
}
