# JobOrbit — Smart Recruitment Platform

A Java full-stack recruitment/job-portal application built for the **CDAC PG-DAC project**.
Monolithic Spring Boot backend (REST API) + React.js frontend + MySQL, with JWT-based
authentication, role-based access control (Admin / Recruiter / Candidate), and a
**smart skill-matching engine** that scores how well a candidate fits a job.

## Tech Stack

| Layer          | Technology                                   |
|----------------|-----------------------------------------------|
| Backend        | Java 17, Spring Boot 3.2.5, Spring Security, Spring Data JPA (Hibernate) |
| Auth           | JWT (jjwt), BCrypt password hashing            |
| Database       | MySQL 8                                        |
| Frontend       | React 18, React Router 6, Axios                |
| Build Tools    | Maven (backend), npm (frontend)                |

## Core Features

- **Auth**: Register/login as Candidate or Recruiter (Admin is seeded, not self-registered), JWT issued on login.
- **Recruiter**: Post/edit/close/delete jobs, view applicants per job, update application status (Applied → Shortlisted/Selected/Rejected).
- **Candidate**: Build a profile (skills, experience, education, resume upload), browse/search jobs, apply with resume + cover letter, track application status.
- **Smart Matching Engine**: `MatchingServiceImpl` compares a candidate's skill set against a job's required skills and returns a percentage match score, used both to rank the candidate's "Recommended Jobs" feed and to show recruiters how well an applicant fits.
- **Admin**: Dashboard with platform stats (users/jobs/applications counts), manage users and job postings.

## Project Structure

```
joborbit/
├── backend/                     Spring Boot monolith (REST API)
│   └── src/main/java/com/joborbit/
│       ├── entity/               JPA entities (User, JobPost, JobApplication, CandidateProfile, Company)
│       ├── repository/           Spring Data JPA repositories
│       ├── dto/                  Request/response DTOs
│       ├── service / service/impl  Business logic (incl. MatchingServiceImpl)
│       ├── controller/           REST controllers
│       ├── security/             JWT filter, JwtUtil, UserDetailsService
│       ├── config/                Security, CORS, static file serving, admin seeder
│       └── exception/            Global exception handling
├── frontend/                    React SPA
│   └── src/{api,context,components,pages}
├── database/
│   └── joborbit_schema.sql       Reference DDL (also auto-created by Hibernate)
└── docs/                         CDAC report outline + interview prep notes
```

## Setup & Run

### 1. Database
Make sure MySQL is running locally. The app auto-creates the `joborbit_db` schema
(`createDatabaseIfNotExist=true` in `application.properties`) — you don't have to run
the SQL file manually, but it's included for reference/documentation.

Update credentials in `backend/src/main/resources/application.properties` if your
MySQL username/password differ from `root` / `root`.

### 2. Backend
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`. On first startup it seeds a default admin:
`admin@joborbit.com` / `Admin@123` (change this password after logging in).

### 3. Frontend
```bash
cd frontend
npm install
npm start
```
Runs on `http://localhost:3000` and proxies `/api` calls to the backend (see `proxy` in `package.json`).

## Key API Endpoints

| Method | Endpoint                                       | Access      |
|--------|-------------------------------------------------|-------------|
| POST   | `/api/auth/register`                            | Public      |
| POST   | `/api/auth/login`                               | Public      |
| GET    | `/api/jobs/public` , `/api/jobs/public/search`   | Public      |
| GET    | `/api/jobs/public/{id}`                          | Public      |
| POST/PUT/DELETE | `/api/recruiter/jobs/**`                | RECRUITER   |
| GET    | `/api/recruiter/applications`                    | RECRUITER   |
| PATCH  | `/api/recruiter/applications/{id}/status`        | RECRUITER   |
| GET    | `/api/candidate/jobs/recommended`                | CANDIDATE   |
| GET/PUT| `/api/candidate/profile`                         | CANDIDATE   |
| POST   | `/api/candidate/applications`                    | CANDIDATE   |
| GET    | `/api/admin/stats` , `/api/admin/users` , `/api/admin/jobs` | ADMIN |

## Notes for the CDAC Submission

- `docs/PROJECT_REPORT_OUTLINE.md` gives a ready-to-fill structure for the written project report (abstract, SRS, modules, ER description, screenshots list).
- `docs/INTERVIEW_QA.md` covers common viva/interview questions on Spring Boot, Spring Security/JWT, Hibernate, REST, and React — geared at explaining this specific project.
- The `ddl-auto=update` setting is convenient for development; for the final submitted build consider switching to `validate` and using the provided SQL script as the source of truth.
