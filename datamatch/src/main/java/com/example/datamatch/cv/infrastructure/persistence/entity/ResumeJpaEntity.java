package com.example.datamatch.cv.infrastructure.persistence.entity;

import com.example.datamatch.cv.domain.model.CvStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumeJpaEntity {

    @Id
    private UUID id;
    
    private String fileName;
    
    private String storagePath;
    
    @Column(columnDefinition = "TEXT")
    private String rawText;
    
    @Enumerated(EnumType.STRING)
    private CvStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> skills;
}
