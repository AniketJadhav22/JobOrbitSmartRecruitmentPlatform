package com.joborbit.controller;

import com.joborbit.dto.ApplicationRequest;
import com.joborbit.dto.ApplicationResponse;
import com.joborbit.dto.ApplicationStatusUpdateRequest;
import com.joborbit.security.UserPrincipal;
import com.joborbit.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping(value = "/api/candidate/applications", consumes = "multipart/form-data")
    public ResponseEntity<ApplicationResponse> apply(@AuthenticationPrincipal UserPrincipal principal,
                                                      @RequestParam Long jobPostId,
                                                      @RequestParam(required = false) String coverLetter,
                                                      @RequestParam(required = false) MultipartFile resume) {
        ApplicationRequest request = new ApplicationRequest();
        request.setJobPostId(jobPostId);
        request.setCoverLetter(coverLetter);
        return ResponseEntity.ok(applicationService.apply(principal.getId(), request, resume));
    }

    @GetMapping("/api/candidate/applications")
    public ResponseEntity<List<ApplicationResponse>> myApplications(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(applicationService.getApplicationsForCandidate(principal.getId()));
    }

    @GetMapping("/api/recruiter/applications")
    public ResponseEntity<List<ApplicationResponse>> allForRecruiter(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(applicationService.getApplicationsForRecruiter(principal.getId()));
    }

    @GetMapping("/api/recruiter/jobs/{jobId}/applications")
    public ResponseEntity<List<ApplicationResponse>> forJob(@AuthenticationPrincipal UserPrincipal principal,
                                                             @PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(principal.getId(), jobId));
    }

    @PatchMapping("/api/recruiter/applications/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(@AuthenticationPrincipal UserPrincipal principal,
                                                             @PathVariable Long id,
                                                             @RequestBody ApplicationStatusUpdateRequest request) {
        return ResponseEntity.ok(applicationService.updateStatus(principal.getId(), id, request.getStatus()));
    }
}
