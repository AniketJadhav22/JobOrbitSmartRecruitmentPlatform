package com.joborbit.repository;

import com.joborbit.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByCandidateId(Long candidateId);
    List<JobApplication> findByJobPostId(Long jobPostId);
    List<JobApplication> findByJobPostPostedById(Long recruiterId);
    Optional<JobApplication> findByJobPostIdAndCandidateId(Long jobPostId, Long candidateId);
    boolean existsByJobPostIdAndCandidateId(Long jobPostId, Long candidateId);
}
