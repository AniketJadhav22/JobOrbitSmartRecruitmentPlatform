package com.joborbit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 4000, nullable = false)
    private String description;

    /** Comma separated required skills, matched against CandidateProfile.skills */
    @Column(length = 1000, nullable = false)
    private String skillsRequired;

    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private Integer minExperience;

    private Double minSalary;

    private Double maxSalary;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private LocalDate applicationDeadline;

    @Column(updatable = false)
    private LocalDateTime postedDate = LocalDateTime.now();
}
