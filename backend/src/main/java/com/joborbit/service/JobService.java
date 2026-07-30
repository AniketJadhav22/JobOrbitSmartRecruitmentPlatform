package com.joborbit.service;

import com.joborbit.dto.JobPostRequest;
import com.joborbit.dto.JobPostResponse;

import java.util.List;

public interface JobService {
    JobPostResponse createJob(Long recruiterId, JobPostRequest request);
    JobPostResponse updateJob(Long recruiterId, Long jobId, JobPostRequest request);
    void closeJob(Long recruiterId, Long jobId);
    void deleteJob(Long recruiterId, Long jobId);
    List<JobPostResponse> getAllActiveJobs();
    List<JobPostResponse> searchJobs(String keyword, String location);
    JobPostResponse getJobById(Long jobId, Long candidateIdOrNull);
    List<JobPostResponse> getJobsByRecruiter(Long recruiterId);
    /** Active jobs ranked by skill-match against the candidate's profile, best match first */
    List<JobPostResponse> getRecommendedJobs(Long candidateId);
}
