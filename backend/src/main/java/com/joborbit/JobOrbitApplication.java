package com.joborbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JobOrbit - Smart Recruitment Platform
 * CDAC PG-DAC Project
 *
 * Entry point of the monolithic Spring Boot backend which exposes
 * REST APIs consumed by the React front-end.
 */
@SpringBootApplication
public class JobOrbitApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobOrbitApplication.class, args);
    }
}
