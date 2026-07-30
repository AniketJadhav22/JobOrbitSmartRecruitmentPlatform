package com.joborbit.dto;

import com.joborbit.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String fullName;

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;

    private String phone;

    @NotNull
    private Role role; // ADMIN registration is blocked in service layer

    // Optional, used when role == RECRUITER
    private String companyName;
}
