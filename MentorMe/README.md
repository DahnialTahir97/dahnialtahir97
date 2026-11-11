# MentorMe — Individual Development Workspace

**Project:** Tutor-Match: Academic Peer Mentoring  
**Developer:** DahnialTahir97  
**Stack:** Java 21, Spring Boot 3.5.x, Maven, Thymeleaf, MySQL, Docker  
**Source of truth:** Lecturer-approved MentorMe ERD.

## Repository separation

This is the individual implementation workspace. The group repository is reference material only. Individual work stays in this repository and on the `mentorme-development` branch.

## Database design — eight ERD tables

1. `faculty`
2. `users`
3. `subject`
4. `tutor_profile`
5. `tutor_subject`
6. `availability`
7. `booking`
8. `review`

See `database/schema.sql` for the current MySQL DDL and `database/seed-development.sql` for non-production sample faculty. The schema was initially transcribed from the ERD notes available during development. Before production, compare every column, datatype, key and relationship with the lecturer-approved ERD; do not treat inferred details as confirmed.

## Technology and deployment architecture

- **GitHub:** source code, version history and automated build/test workflow.
- **Java 21 + Spring Boot:** application and server-side business logic.
- **Thymeleaf:** server-rendered web interface.
- **MySQL:** relational persistence.
- **Docker:** reproducible Java runtime image.
- **Vercel:** target deployment platform. Vercel supports OCI container images via Vercel Functions. The Vercel-specific image is `Dockerfile.vercel`. When creating the Vercel project, set the project Root Directory to `MentorMe` so Vercel can detect that file. Confirm current account/plan limits and container availability before production deployment.

The current application is a single Spring Boot web application (UI and server-side logic together), packaged as a container. It is not a separate React/Next.js frontend plus REST API. This avoids maintaining two separate codebases for the current project.

## Run and configure

Required: JDK 21, Maven 3.9+, and MySQL 8.0+.

1. Create a local database named `mentorme_db`.
2. Execute `database/schema.sql` in MySQL.
3. Optionally execute `database/seed-development.sql` for sample faculty rows.
4. Set environment variables:
   - `DB_URL=jdbc:mysql://localhost:3306/mentorme_db`
   - `DB_USERNAME=your_local_mysql_user`
   - `DB_PASSWORD=your_local_mysql_password`
   - `PORT=8080` (optional)
5. Run `mvn spring-boot:run` from this `MentorMe` directory.
6. To load fictional demo records, set `MENTORME_DEMO_DATA_ENABLED=true` before starting the app. The seeder is disabled by default and can be safely run more than once.\n7. Open `http://localhost:8080`.\n\n### Demo accounts\n\nWhen demo seeding is enabled, use these accounts (all fictional):\n\n| Role | Email | Password |\n|---|---|---|\n| Admin | `admin@mentorme.demo` | `AdminDemo123!` |\n| Tutor | `aisyah.tutor@mentorme.demo` | `TutorDemo123!` |\n| Tutor | `daniel.tutor@mentorme.demo` | `TutorDemo123!` |\n| Student | `iman.student@mentorme.demo` | `StudentDemo123!` |\n| Student | `amir.student@mentorme.demo` | `StudentDemo123!` |\n| Student | `sofia.student@mentorme.demo` | `StudentDemo123!` |\n\nDemo accounts are for coursework/testing only. Do not enable the demo seeder on a public production database; it creates predictable demo credentials.

Build check: `mvn --batch-mode verify`.

Container checks: `docker build -t mentorme:local .` and `docker build -f Dockerfile.vercel -t mentorme:vercel .` from this directory.

## Production configuration principles

- Production database credentials must be stored as deployment environment secrets, never in Git.
- Keep `spring.jpa.hibernate.ddl-auto=validate`; database structure changes must be reviewed and applied through versioned SQL migrations.
- Public registration must never allow users to select the Admin role.
- Authorization must be enforced on the server.
- Booking creation must validate the tutor, selected availability, date and slot conflict inside a database transaction.
- Cloud MySQL TLS/network settings must be confirmed with the selected provider before deployment.

## Current progress

See `docs/DEVELOPMENT_STATUS.md` for completed work, verification status and remaining features. This is still under development; do not treat the current dashboards as a finished booking platform.
