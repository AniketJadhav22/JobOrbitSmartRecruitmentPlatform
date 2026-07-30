package com.joborbit.controller;

import com.joborbit.dto.CandidateProfileRequest;
import com.joborbit.security.UserPrincipal;
import com.joborbit.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/candidate/profile")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<Object> getProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(candidateService.getProfile(principal.getId()));
    }

    @PutMapping
    public ResponseEntity<Object> upsertProfile(@AuthenticationPrincipal UserPrincipal principal,
                                                 @RequestBody CandidateProfileRequest request) {
        return ResponseEntity.ok(candidateService.upsertProfile(principal.getId(), request));
    }

    @PostMapping(value = "/resume", consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadResume(@AuthenticationPrincipal UserPrincipal principal,
                                                @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(java.util.Map.of("resumeFilePath", candidateService.uploadResume(principal.getId(), file)));
    }
}
