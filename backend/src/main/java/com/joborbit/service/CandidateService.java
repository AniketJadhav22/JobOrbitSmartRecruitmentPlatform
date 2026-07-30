package com.joborbit.service;

import com.joborbit.dto.CandidateProfileRequest;

public interface CandidateService {
    Object getProfile(Long userId);
    Object upsertProfile(Long userId, CandidateProfileRequest request);
    String uploadResume(Long userId, org.springframework.web.multipart.MultipartFile file);
}
