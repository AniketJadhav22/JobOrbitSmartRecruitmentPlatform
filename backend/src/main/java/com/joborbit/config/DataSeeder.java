package com.joborbit.config;

import com.joborbit.entity.Role;
import com.joborbit.entity.User;
import com.joborbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default ADMIN account on first startup, since admin
 * accounts cannot be created via the public /api/auth/register endpoint.
 * Default login: admin@joborbit.com / Admin@123  (change after first login)
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@joborbit.com")) {
            User admin = new User();
            admin.setFullName("JobOrbit Admin");
            admin.setEmail("admin@joborbit.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Seeded default admin: admin@joborbit.com / Admin@123");
        }
    }
}
