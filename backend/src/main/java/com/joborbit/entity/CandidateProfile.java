package com.joborbit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Extended profile information for users with role CANDIDATE.
 * Holds the data used by the smart skill-matching engine.
 */
@Entity
@Table(name = "candidate_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Comma separated list of skills, e.g. "Java,Spring Boot,React,MySQL" */
    @Column(length = 1000)
    private String skills;

    private Integer experienceYears;

    private String education;

    private String resumeFilePath;

    @Column(length = 2000)
    private String bio;
}
