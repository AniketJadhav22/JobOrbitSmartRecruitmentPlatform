package com.joborbit.dto;

import com.joborbit.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
    private Long id;
    private Long jobPostId;
    private String jobTitle;
    private Long candidateId;
    private String candidateName;
    private String resumeFilePath;
    private String coverLetter;
    private ApplicationStatus status;
    private Double matchScore;
    private LocalDateTime appliedDate;
}
