package com.joborbit.service;

import com.joborbit.dto.JwtResponse;
import com.joborbit.dto.LoginRequest;
import com.joborbit.dto.RegisterRequest;

public interface AuthService {
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
}
