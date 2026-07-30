package com.joborbit.dto;

import com.joborbit.entity.JobType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobPostRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String skillsRequired;
    private String location;
    private JobType jobType;
    private Integer minExperience;
    private Double minSalary;
    private Double maxSalary;
    private LocalDate applicationDeadline;
}
