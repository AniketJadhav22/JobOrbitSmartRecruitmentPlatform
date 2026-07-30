package com.joborbit.service;

import com.joborbit.dto.ApplicationRequest;
import com.joborbit.dto.ApplicationResponse;
import com.joborbit.entity.ApplicationStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse apply(Long candidateId, ApplicationRequest request, MultipartFile resume);
    List<ApplicationResponse> getApplicationsForCandidate(Long candidateId);
    List<ApplicationResponse> getApplicationsForJob(Long recruiterId, Long jobId);
    List<ApplicationResponse> getApplicationsForRecruiter(Long recruiterId);
    ApplicationResponse updateStatus(Long recruiterId, Long applicationId, ApplicationStatus status);
}
