package com.joborbit.service.impl;

import com.joborbit.dto.ApplicationRequest;
import com.joborbit.dto.ApplicationResponse;
import com.joborbit.entity.*;
import com.joborbit.exception.BadRequestException;
import com.joborbit.exception.ResourceNotFoundException;
import com.joborbit.repository.CandidateProfileRepository;
import com.joborbit.repository.JobApplicationRepository;
import com.joborbit.repository.JobPostRepository;
import com.joborbit.repository.UserRepository;
import com.joborbit.service.ApplicationService;
import com.joborbit.service.FileStorageService;
import com.joborbit.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final FileStorageService fileStorageService;
    private final MatchingService matchingService;

    @Override
    public ApplicationResponse apply(Long candidateId, ApplicationRequest request, MultipartFile resume) {
        JobPost job = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (job.getStatus() == JobStatus.CLOSED) {
            throw new BadRequestException("This job is no longer accepting applications");
        }
        if (applicationRepository.existsByJobPostIdAndCandidateId(job.getId(), candidateId)) {
            throw new BadRequestException("You have already applied to this job");
        }

        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        String resumePath = resume != null && !resume.isEmpty()
                ? fileStorageService.store(resume, "applications")
                : candidateProfileRepository.findByUserId(candidateId)
                    .map(CandidateProfile::getResumeFilePath).orElse(null);

        String candidateSkills = candidateProfileRepository.findByUserId(candidateId)
                .map(CandidateProfile::getSkills).orElse("");
        double score = matchingService.computeMatchScore(candidateSkills, job.getSkillsRequired());

        JobApplication application = new JobApplication();
        application.setJobPost(job);
        application.setCandidate(candidate);
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeFilePath(resumePath);
        application.setMatchScore(score);
        application.setStatus(ApplicationStatus.APPLIED);

        return toResponse(applicationRepository.save(application));
    }

    @Override
    public List<ApplicationResponse> getApplicationsForCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponse> getApplicationsForJob(Long recruiterId, Long jobId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not own this job posting");
        }
        return applicationRepository.findByJobPostId(jobId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponse> getApplicationsForRecruiter(Long recruiterId) {
        return applicationRepository.findByJobPostPostedById(recruiterId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ApplicationResponse updateStatus(Long recruiterId, Long applicationId, ApplicationStatus status) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (!application.getJobPost().getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not own the job for this application");
        }
        application.setStatus(status);
        return toResponse(applicationRepository.save(application));
    }

    private ApplicationResponse toResponse(JobApplication a) {
        return ApplicationResponse.builder()
                .id(a.getId())
                .jobPostId(a.getJobPost().getId())
                .jobTitle(a.getJobPost().getTitle())
                .candidateId(a.getCandidate().getId())
                .candidateName(a.getCandidate().getFullName())
                .resumeFilePath(a.getResumeFilePath())
                .coverLetter(a.getCoverLetter())
                .status(a.getStatus())
                .matchScore(a.getMatchScore())
                .appliedDate(a.getAppliedDate())
                .build();
    }
}
