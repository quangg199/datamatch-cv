package com.example.datamatch.cv.application.ports;

import com.example.datamatch.cv.domain.model.Resume;
import java.util.Optional;
import java.util.UUID;

public interface ResumeRepositoryPort {
    void save(Resume resume);
    Optional<Resume> findById(UUID id);
}
