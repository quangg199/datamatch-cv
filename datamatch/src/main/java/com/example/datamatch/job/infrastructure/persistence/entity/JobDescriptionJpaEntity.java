package com.example.datamatch.job.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "job_descriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDescriptionJpaEntity {

    @Id
    private UUID id;
    
    private String title;
    
    private String companyName;
    
    @Column(columnDefinition = "TEXT")
    private String rawDescription;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> requiredSkills;
}
