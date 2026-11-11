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
- Java 21 multi-stage Dockerfile for container-based deployment

## Verification status

- GitHub Actions has been configured to compile and test every relevant push.
- The last recorded workflow run failed because the registration service could not call the protected JPA entity constructor. That issue has been corrected in the latest commit; the new workflow result must be checked before calling the build green.
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

- Vercel: container image is prepared with the Java 21 Dockerfile. Vercel's current container/backend offering and account-plan availability must be confirmed at deployment time.
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
