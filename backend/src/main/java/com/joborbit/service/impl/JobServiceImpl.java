package com.joborbit.service.impl;

import com.joborbit.dto.JobPostRequest;
import com.joborbit.dto.JobPostResponse;
import com.joborbit.entity.*;
import com.joborbit.exception.BadRequestException;
import com.joborbit.exception.ResourceNotFoundException;
import com.joborbit.repository.CandidateProfileRepository;
import com.joborbit.repository.CompanyRepository;
import com.joborbit.repository.JobPostRepository;
import com.joborbit.repository.UserRepository;
import com.joborbit.service.JobService;
import com.joborbit.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final MatchingService matchingService;

    @Override
    public JobPostResponse createJob(Long recruiterId, JobPostRequest request) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));

        JobPost job = new JobPost();
        applyRequest(job, request);
        job.setPostedBy(recruiter);
        companyRepository.findByRecruiterId(recruiterId).ifPresent(job::setCompany);

        return toResponse(jobPostRepository.save(job), null);
    }

    @Override
    public JobPostResponse updateJob(Long recruiterId, Long jobId, JobPostRequest request) {
        JobPost job = getOwnedJob(recruiterId, jobId);
        applyRequest(job, request);
        return toResponse(jobPostRepository.save(job), null);
    }

    @Override
    public void closeJob(Long recruiterId, Long jobId) {
        JobPost job = getOwnedJob(recruiterId, jobId);
        job.setStatus(JobStatus.CLOSED);
        jobPostRepository.save(job);
    }

    @Override
    public void deleteJob(Long recruiterId, Long jobId) {
        JobPost job = getOwnedJob(recruiterId, jobId);
        jobPostRepository.delete(job);
    }

    @Override
    public List<JobPostResponse> getAllActiveJobs() {
        return jobPostRepository.findByStatus(JobStatus.ACTIVE).stream()
                .map(j -> toResponse(j, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponse> searchJobs(String keyword, String location) {
        return jobPostRepository.searchJobs(
                    (keyword == null || keyword.isBlank()) ? null : keyword,
                    (location == null || location.isBlank()) ? null : location)
                .stream()
                .map(j -> toResponse(j, null))
                .collect(Collectors.toList());
    }

    @Override
    public JobPostResponse getJobById(Long jobId, Long candidateIdOrNull) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        Double score = candidateIdOrNull == null ? null : computeScoreForCandidate(candidateIdOrNull, job);
        return toResponse(job, score);
    }

    @Override
    public List<JobPostResponse> getJobsByRecruiter(Long recruiterId) {
        return jobPostRepository.findByPostedById(recruiterId).stream()
                .map(j -> toResponse(j, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponse> getRecommendedJobs(Long candidateId) {
        List<JobPost> activeJobs = jobPostRepository.findByStatus(JobStatus.ACTIVE);
        String candidateSkills = candidateProfileRepository.findByUserId(candidateId)
                .map(CandidateProfile::getSkills).orElse("");

        return activeJobs.stream()
                .map(j -> toResponse(j, matchingService.computeMatchScore(candidateSkills, j.getSkillsRequired())))
                .sorted(Comparator.comparing(JobPostResponse::getMatchScore, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    // ---- helpers ----

    private Double computeScoreForCandidate(Long candidateId, JobPost job) {
        String candidateSkills = candidateProfileRepository.findByUserId(candidateId)
                .map(CandidateProfile::getSkills).orElse("");
        return matchingService.computeMatchScore(candidateSkills, job.getSkillsRequired());
    }

    private JobPost getOwnedJob(Long recruiterId, Long jobId) {
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        if (!job.getPostedBy().getId().equals(recruiterId)) {
            throw new BadRequestException("You do not own this job posting");
        }
        return job;
    }

    private void applyRequest(JobPost job, JobPostRequest request) {
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setMinExperience(request.getMinExperience());
        job.setMinSalary(request.getMinSalary());
        job.setMaxSalary(request.getMaxSalary());
        job.setApplicationDeadline(request.getApplicationDeadline());
    }

    private JobPostResponse toResponse(JobPost job, Double matchScore) {
        return JobPostResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .skillsRequired(job.getSkillsRequired())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .minExperience(job.getMinExperience())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .status(job.getStatus())
                .companyName(job.getCompany() != null ? job.getCompany().getName() : null)
                .recruiterId(job.getPostedBy().getId())
                .recruiterName(job.getPostedBy().getFullName())
                .applicationDeadline(job.getApplicationDeadline())
                .postedDate(job.getPostedDate())
                .matchScore(matchScore)
                .build();
    }
}
