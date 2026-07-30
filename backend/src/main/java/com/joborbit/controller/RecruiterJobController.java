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

@RestController
@RequestMapping("/api/recruiter/jobs")
@RequiredArgsConstructor
public class RecruiterJobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobPostResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                   @Valid @RequestBody JobPostRequest request) {
        return ResponseEntity.ok(jobService.createJob(principal.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobPostResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody JobPostRequest request) {
        return ResponseEntity.ok(jobService.updateJob(principal.getId(), id, request));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> close(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        jobService.closeJob(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        jobService.deleteJob(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<JobPostResponse>> myJobs(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(principal.getId()));
    }
}
