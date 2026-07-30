package com.joborbit.controller;

import com.joborbit.dto.JobPostRequest;
import com.joborbit.dto.JobPostResponse;
import com.joborbit.security.UserPrincipal;
import com.joborbit.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public, unauthenticated job browsing endpoints.
 * Recruiter/candidate specific actions live under JobRecruiterController / JobCandidateController.
 */
@RestController
@RequestMapping("/api/jobs/public")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> getAllActiveJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPostResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(jobService.searchJobs(keyword, location));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostResponse> getJob(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserPrincipal principal) {
        Long candidateId = (principal != null && "CANDIDATE".equals(principal.getRole())) ? principal.getId() : null;
        return ResponseEntity.ok(jobService.getJobById(id, candidateId));
    }
}
