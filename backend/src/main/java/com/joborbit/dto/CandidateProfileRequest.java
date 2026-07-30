package com.joborbit.dto;

import lombok.Data;

@Data
public class CandidateProfileRequest {
    private String skills;
    private Integer experienceYears;
    private String education;
    private String bio;
}
