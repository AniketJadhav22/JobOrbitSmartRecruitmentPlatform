package com.joborbit.dto;

import com.joborbit.entity.JobStatus;
import com.joborbit.entity.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostResponse {
    private Long id;
    private String title;
    private String description;
    private String skillsRequired;
    private String location;
    private JobType jobType;
    private Integer minExperience;
    private Double minSalary;
    private Double maxSalary;
    private JobStatus status;
    private String companyName;
    private Long recruiterId;
    private String recruiterName;
    private LocalDate applicationDeadline;
    private LocalDateTime postedDate;
    /** Populated only when requested by a logged-in candidate */
    private Double matchScore;
}
