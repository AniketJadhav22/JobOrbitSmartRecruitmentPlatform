# JobOrbit — Interview / Viva Preparation

Practical answers geared specifically to how this project is built, so you can defend
design choices confidently.

## Project-specific

**Q: Walk me through the architecture.**
A React SPA calls a Spring Boot REST API over HTTPS/JSON. Every authenticated request
carries a JWT in the `Authorization: Bearer <token>` header. `JwtAuthFilter` (a
`OncePerRequestFilter`) validates the token, loads the user, and populates Spring
Security's context before the request reaches the controller. Controllers delegate to
services, services use Spring Data JPA repositories, Hibernate maps entities to MySQL
tables.

**Q: Why did you choose a monolith instead of microservices?**
A: For a project of this size (a handful of related domains: users, jobs,
applications) a monolith keeps deployment, transactions, and development simple —
microservices would add network overhead, distributed transaction complexity, and
operational burden without a corresponding benefit at this scale. It also matches the
scope expected of a PG-DAC project.

**Q: How does the "smart" matching feature actually work?**
A: `MatchingServiceImpl.computeMatchScore` splits both the candidate's stored skills and
the job's required skills on commas, normalizes case/whitespace into sets, and returns
`(matched skills / total required skills) * 100`. It's intentionally simple and
explainable — a strength when asked "why is my match score X%?" — but the design
(returning a plain interface, `MatchingService`) leaves room to swap in a weighted or
NLP-based algorithm later without touching callers.

**Q: How do you prevent a candidate from applying to the same job twice?**
A: A unique DB constraint on `(job_post_id, candidate_id)` in `job_applications`, plus
an explicit `existsByJobPostIdAndCandidateId` check in `ApplicationServiceImpl` that
throws a friendly `BadRequestException` before hitting the DB constraint.

**Q: How is authorization enforced beyond just authentication?**
A: Two layers: (1) URL-pattern rules in `SecurityConfig` (`/api/recruiter/**` requires
`ROLE_RECRUITER`, etc.), and (2) ownership checks inside services — e.g.
`JobServiceImpl.getOwnedJob` verifies the job actually belongs to the recruiter making
the request, so one recruiter can't edit another's postings even though both hold a
valid RECRUITER-role token.

**Q: Where are resumes stored?**
A: On the local filesystem under the configured `app.upload.dir`, served back via a
Spring `ResourceHandler` mapped to `/uploads/**`. For production this would be swapped
for object storage (e.g. S3) — the `FileStorageService` interface is the seam where
that change would happen.

## Spring Boot / Spring Security

**Q: What is Spring Boot auto-configuration?**
A: Spring Boot inspects the classpath and existing beans, then auto-configures sensible
defaults (e.g. a `DataSource` when a JDBC driver is present). `@SpringBootApplication`
combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`.

**Q: Why JWT instead of session-based auth?**
A: JWT is stateless — the server doesn't need to store session data, which fits a REST
API cleanly and scales horizontally without sticky sessions. The trade-off is that
revoking a single token before expiry is harder than invalidating a server-side
session; short expirations and refresh-token patterns mitigate that in production.

**Q: What does `@EnableMethodSecurity` / the filter chain do here?**
A: It lets you additionally annotate service/controller methods with
`@PreAuthorize` if needed; in this project the primary enforcement is the
`authorizeHttpRequests` matcher rules in `SecurityConfig`.

## Hibernate / JPA

**Q: What's the difference between `ddl-auto=update` and `validate`?**
A: `update` lets Hibernate alter the schema to match your entities automatically —
convenient in development, risky in production (it can silently change a live schema).
`validate` just checks the entities against the existing schema and fails fast if they
don't match, which is safer once you have a real migration tool (Flyway/Liquibase) or a
hand-maintained script like `database/joborbit_schema.sql`.

**Q: Explain the relationships in this schema.**
A: `User` has a one-to-one with `CandidateProfile` (for candidates) and with `Company`
(for recruiters). `JobPost` has a many-to-one to the recruiter `User` and to `Company`.
`JobApplication` has many-to-one relationships to both `JobPost` and the candidate
`User`, with a composite uniqueness constraint preventing duplicate applications.

## REST API design

**Q: Why separate controllers for public/recruiter/candidate job endpoints instead of
one JobController with internal role checks?**
A: It keeps the URL space self-documenting and lets `SecurityConfig`'s path-based rules
do the authorization declaratively (`/api/recruiter/**` vs `/api/candidate/**`) rather
than scattering `if (role == ...)` checks through one class.

## React frontend

**Q: How does the frontend know if a user is logged in, and how are protected pages
handled?**
A: `AuthContext` holds the current user (persisted to `localStorage` alongside the JWT)
and exposes `login`/`register`/`logout`. `ProtectedRoute` reads that context and
redirects to `/login` (or `/`) if the user is missing or lacks the required role.
Axios attaches the token to every request via a request interceptor, and a response
interceptor auto-logs-out on a 401.

**Q: Why store the JWT in `localStorage` instead of a cookie?**
A: Simpler for a REST API consumed by a decoupled SPA and avoids CSRF concerns tied to
cookie-based auth, at the cost of some XSS exposure if the app were vulnerable to script
injection — worth mentioning as a known trade-off if asked about security hardening.
