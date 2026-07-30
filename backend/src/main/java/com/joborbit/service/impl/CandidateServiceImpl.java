package com.joborbit.service.impl;

import com.joborbit.dto.CandidateProfileRequest;
import com.joborbit.entity.CandidateProfile;
import com.joborbit.entity.User;
import com.joborbit.exception.ResourceNotFoundException;
import com.joborbit.repository.CandidateProfileRepository;
import com.joborbit.repository.UserRepository;
import com.joborbit.service.CandidateService;
import com.joborbit.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Object getProfile(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public Object upsertProfile(Long userId, CandidateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CandidateProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CandidateProfile p = new CandidateProfile();
                    p.setUser(user);
                    return p;
                });

        profile.setSkills(request.getSkills());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setEducation(request.getEducation());
        profile.setBio(request.getBio());

        return profileRepository.save(profile);
    }

    @Override
    public String uploadResume(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String path = fileStorageService.store(file, "resumes");

        CandidateProfile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CandidateProfile p = new CandidateProfile();
                    p.setUser(user);
                    return p;
                });
        profile.setResumeFilePath(path);
        profileRepository.save(profile);
        return path;
    }
}
