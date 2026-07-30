package com.joborbit.controller;

import com.joborbit.dto.JobPostResponse;
import com.joborbit.security.UserPrincipal;
import com.joborbit.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/jobs")
@RequiredArgsConstructor
public class CandidateJobController {

    private final JobService jobService;

    /** Active jobs ranked by smart skill-match score, best fit first. */
    @GetMapping("/recommended")
    public ResponseEntity<List<JobPostResponse>> recommended(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(jobService.getRecommendedJobs(principal.getId()));
    }
}
