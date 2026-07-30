# JobOrbit — Smart Recruitment Platform
## CDAC PG-DAC Project Report — Outline

Fill in the bracketed sections with your batch/center details, screenshots, and any
customizations you make. This mirrors the structure CDAC project reports typically expect.

### 1. Title Page
- Project Title: JobOrbit — Smart Recruitment Platform
- Center / Batch: [fill in]
- Team Members & Roles: [fill in]
- Guide/Mentor Name: [fill in]

### 2. Abstract
JobOrbit is a Java full-stack web application that digitizes the recruitment process by
connecting job seekers (candidates) with employers (recruiters) through a role-based
portal. Its distinguishing feature is a **skill-based matching engine** that scores each
candidate against each job posting, letting candidates see recommended jobs ranked by
fit and letting recruiters instantly gauge applicant suitability.

### 3. Problem Statement
Traditional job portals present a flat list of postings with no indication of fit,
forcing candidates to manually read every requirement and forcing recruiters to
manually screen every resume. JobOrbit addresses this by computing an explicit,
transparent match percentage between a candidate's declared skills and a job's
required skills.

### 4. Objectives
- Provide separate, secure workflows for Admin, Recruiter, and Candidate roles.
- Allow recruiters to publish, edit, and manage the lifecycle of job postings.
- Allow candidates to maintain a profile (skills, experience, resume) and apply to jobs.
- Compute and surface a skill-match score to both parties.
- Provide an admin console for platform oversight (users, jobs, stats).

### 5. Scope
In scope: authentication, job CRUD, application workflow, skill matching, resume
upload, admin oversight.
Out of scope (documented as future work): email/SMS notifications, payment/subscription
plans, AI-based resume parsing, chat between recruiter and candidate.

### 6. System Requirements Specification (SRS)
- **Functional requirements**: see Section 8 (Modules).
- **Non-functional requirements**: JWT-based stateless auth, BCrypt password hashing,
  role-based authorization at the API layer, responsive React UI.
- **Hardware/Software**: Java 17, Spring Boot 3.2.5, MySQL 8, Node.js 18+, any modern browser.

### 7. Technology Stack
Java 17 · Spring Boot · Spring Security · Spring Data JPA / Hibernate · MySQL ·
React 18 · React Router · Axios · JWT (jjwt) · Maven · npm.

### 8. Modules

1. **Authentication & Authorization module** — registration, login, JWT issuance,
   role-based route/API protection.
2. **Candidate module** — profile management, resume upload, job browsing/search,
   recommended jobs (matching engine), applying to jobs, tracking application status.
3. **Recruiter module** — company profile (auto-created at registration), job posting
   CRUD, viewing/managing applicants, updating application status.
4. **Admin module** — platform-wide statistics, user management, job oversight.
5. **Smart Matching module** — `MatchingServiceImpl`, the core algorithm comparing
   comma-separated skill lists and returning a 0–100% score.

### 9. ER Diagram (entities & relationships)
- `User` (1) — (1) `CandidateProfile`
- `User` (1) — (1) `Company` (for recruiters)
- `User` (1, recruiter) — (many) `JobPost`
- `JobPost` (1) — (many) `JobApplication`
- `User` (1, candidate) — (many) `JobApplication`
[Insert a drawn ER diagram image here for the submission — the relationships above map
directly to the entity classes in `backend/src/main/java/com/joborbit/entity/`.]

### 10. Architecture Diagram
React SPA (client) → Axios (JWT in Authorization header) → Spring Boot REST API
→ Spring Security filter chain (JwtAuthFilter) → Controller → Service → Repository
(Spring Data JPA) → MySQL.
[Insert a drawn architecture diagram for the submission.]

### 11. Screens / UI Walkthrough
List each page with a screenshot: Home, Register, Login, Job List/Search, Job Details
+ Apply form, Candidate Dashboard (Recommended Jobs / My Applications / Profile),
Recruiter Dashboard (My Jobs / Post a Job / Applications), Admin Dashboard.

### 12. Testing
Document manual test cases: register as each role, login with wrong password (expect
401), post a job as recruiter, apply as candidate (expect match score computed), attempt
recruiter action with candidate token (expect 403), close/delete a job, admin deletes a
user/job.

### 13. Future Enhancements
Email notifications on status change, AI/NLP-based resume parsing to auto-fill skills,
interview scheduling, in-app messaging, analytics dashboard for recruiters.

### 14. Conclusion
Summarize how the project meets the stated objectives and what you learned about
full-stack development, security, and REST API design.

### 15. References
Spring Boot docs, Spring Security docs, React docs, MySQL docs, JWT (jjwt) docs.
