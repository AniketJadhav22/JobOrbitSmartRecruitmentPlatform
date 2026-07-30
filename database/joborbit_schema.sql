-- =====================================================================
-- JobOrbit - Smart Recruitment Platform
-- Reference DDL (Hibernate ddl-auto=update will also generate/update
-- these automatically on application startup — this file is provided
-- for the CDAC project report / viva and for manual DB inspection).
-- =====================================================================

CREATE DATABASE IF NOT EXISTS joborbit_db;
USE joborbit_db;

-- ---------------------------------------------------------------
-- USERS  (Admin / Recruiter / Candidate share one table + role enum)
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN','RECRUITER','CANDIDATE') NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------
-- CANDIDATE PROFILES  (1-1 with users where role = CANDIDATE)
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS candidate_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    skills VARCHAR(1000),
    experience_years INT,
    education VARCHAR(255),
    resume_file_path VARCHAR(500),
    bio VARCHAR(2000),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------
-- COMPANIES  (1-1 with users where role = RECRUITER)
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    website VARCHAR(255),
    description VARCHAR(2000),
    location VARCHAR(150),
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------
-- JOB POSTS
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS job_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    skills_required VARCHAR(1000) NOT NULL,
    location VARCHAR(150),
    job_type ENUM('FULL_TIME','PART_TIME','INTERNSHIP','CONTRACT'),
    min_experience INT,
    min_salary DOUBLE,
    max_salary DOUBLE,
    status ENUM('ACTIVE','CLOSED') DEFAULT 'ACTIVE',
    recruiter_id BIGINT NOT NULL,
    company_id BIGINT,
    application_deadline DATE,
    posted_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE SET NULL
);

-- ---------------------------------------------------------------
-- JOB APPLICATIONS  (a candidate can apply to a job only once)
-- ---------------------------------------------------------------
CREATE TABLE IF NOT EXISTS job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_post_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    cover_letter VARCHAR(2000),
    resume_file_path VARCHAR(500),
    status ENUM('APPLIED','SHORTLISTED','REJECTED','SELECTED') DEFAULT 'APPLIED',
    match_score DOUBLE,
    applied_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_job_candidate (job_post_id, candidate_id),
    FOREIGN KEY (job_post_id) REFERENCES job_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------
-- Default admin (also auto-seeded by DataSeeder.java on first boot)
-- Password below is the bcrypt hash of "Admin@123"
-- ---------------------------------------------------------------
-- INSERT INTO users (full_name, email, password, role)
-- VALUES ('JobOrbit Admin', 'admin@joborbit.com', '$2a$10$...bcrypt-hash...', 'ADMIN');
