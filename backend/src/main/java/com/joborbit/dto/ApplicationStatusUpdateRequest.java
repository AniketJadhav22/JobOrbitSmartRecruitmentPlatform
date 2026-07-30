package com.joborbit.dto;

import com.joborbit.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationStatusUpdateRequest {
    @NotNull
    private ApplicationStatus status;
}
