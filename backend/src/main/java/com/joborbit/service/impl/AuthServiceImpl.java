package com.joborbit.service.impl;

import com.joborbit.dto.JwtResponse;
import com.joborbit.dto.LoginRequest;
import com.joborbit.dto.RegisterRequest;
import com.joborbit.entity.Company;
import com.joborbit.entity.Role;
import com.joborbit.entity.User;
import com.joborbit.exception.BadRequestException;
import com.joborbit.repository.CompanyRepository;
import com.joborbit.repository.UserRepository;
import com.joborbit.security.JwtUtil;
import com.joborbit.security.UserPrincipal;
import com.joborbit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        if (request.getRole() == Role.ADMIN) {
            throw new BadRequestException("Admin accounts cannot be self-registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user = userRepository.save(user);

        if (request.getRole() == Role.RECRUITER) {
            Company company = new Company();
            company.setRecruiter(user);
            company.setName(request.getCompanyName() != null ? request.getCompanyName() : request.getFullName() + "'s Company");
            companyRepository.save(company);
        }

        UserPrincipal principal = new UserPrincipal(user);
        String token = jwtUtil.generateToken(principal);
        return new JwtResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtil.generateToken(principal);
        return new JwtResponse(token, principal.getId(), principal.getFullName(), principal.getEmail(), principal.getRole());
    }
}
