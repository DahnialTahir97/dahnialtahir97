# MentorMe development status

## Completed in the personal GitHub branch

- Personal branch: `mentorme-development`
- Java 21 / Spring Boot / Maven project structure
- MySQL schema for the eight tables currently represented in the ERD
- Eight JPA entity classes and eight Spring Data repositories
- Landing page, login and registration pages
- BCrypt password hashing and role-based route authorization
- Student, Tutor and Admin dashboard starting pages
- Development faculty seed data
- GitHub Actions Maven build workflow
- Java 21 multi-stage Dockerfile for local/container deployment
- Vercel-specific `Dockerfile.vercel` entrypoint
- Exact dashboard URL role checks (`/admin`, `/tutor`, `/student`) are enforced

## Verification status

- GitHub Actions has been configured to compile and test every relevant push.
- GitHub Actions run #20 completed successfully, including Maven verification and the standard Docker image build.
- A follow-up hardening change fixed role matching for exact dashboard URLs and added a Vercel-specific container build. Its new CI run must pass before considering that change verified.
- Cloud database connectivity and live deployment have not been tested.
- The database schema still needs a final column-by-column comparison with the lecturer-approved ERD before production data is created.

## Remaining application work

1. Admin: faculty and subject CRUD, plus secure first-admin bootstrap.
2. Tutor: profile editing, subject selection and availability management.
3. Student: tutor search and filtering.
4. Booking: transactional creation, slot conflict prevention, status transitions and authorization.
5. Review: one eligible review per completed booking, rating validation and display.
6. Automated tests: repositories, services, authorization, booking concurrency and validation.
7. Production hardening: error pages, validation messages, logging, health endpoint, security headers and database migration strategy.

## Deployment preparation

- Vercel: `Dockerfile.vercel` is prepared. Set Vercel Root Directory to `MentorMe`; Vercel documents OCI container images through Vercel Functions. Confirm current plan limits and deployment eligibility at setup time.
- MySQL: schema and environment-variable based JDBC configuration are prepared. A managed MySQL instance, TLS requirements and credentials will be configured later.
- Never commit production passwords, database URLs containing credentials, API keys or other secrets.
- Do not use Hibernate schema auto-update in production. Keep `spring.jpa.hibernate.ddl-auto=validate`.

## Environment variables

- `PORT` (optional; defaults to 8080)
- `DB_URL` (JDBC URL, e.g. `jdbc:mysql://HOST:3306/mentorme_db`)
- `DB_USERNAME`
- `DB_PASSWORD`

## Important scope note

GitHub is the source-code and CI platform. Vercel is the deployment platform. MySQL is the persistent database. Creating the cloud resources and adding their secrets is a separate deployment step and has intentionally not been done yet.
