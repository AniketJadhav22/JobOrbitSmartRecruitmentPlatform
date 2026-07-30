package com.joborbit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequest {
    @NotNull
    private Long jobPostId;
    private String coverLetter;
}
