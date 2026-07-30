package com.joborbit.controller;

import com.joborbit.entity.Role;
import com.joborbit.entity.User;
import com.joborbit.repository.JobApplicationRepository;
import com.joborbit.repository.JobPostRepository;
import com.joborbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplicationRepository applicationRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/recruiters")
    public ResponseEntity<List<User>> getRecruiters() {
        return ResponseEntity.ok(userRepository.findByRole(Role.RECRUITER));
    }

    @GetMapping("/users/candidates")
    public ResponseEntity<List<User>> getCandidates() {
        return ResponseEntity.ok(userRepository.findByRole(Role.CANDIDATE));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobs() {
        return ResponseEntity.ok(jobPostRepository.findAll());
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobPostRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> stats() {
        return ResponseEntity.ok(Map.of(
                "totalUsers", userRepository.count(),
                "totalRecruiters", (long) userRepository.findByRole(Role.RECRUITER).size(),
                "totalCandidates", (long) userRepository.findByRole(Role.CANDIDATE).size(),
                "totalJobs", jobPostRepository.count(),
                "totalApplications", applicationRepository.count()
        ));
    }
}
