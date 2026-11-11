# MentorMe — Individual Development Workspace

**Project:** Tutor-Match: Academic Peer Mentoring  
**Developer:** DahnialTahir97  
**Stack:** Java 21, Spring Boot 3.5.x, Maven, Thymeleaf, MySQL  
**Database authority:** Final `ERD_MentorMe.pdf` supplied for this individual project.

## Repository separation

This is the developer's personal implementation workspace. The group repository is a reference only. Do not push individual work to the group repository.

## Final ERD — eight tables

1. `faculty`: `faculty_id`, `name`
2. `users`: `user_id`, `full_name`, `email`, `password`, `role`, `faculty_id`
3. `subject`: `subject_id`, `subject_code`, `subject_name`, `faculty_id`
4. `tutor_profile`: `tutor_profile_id`, `user_id`, `bio`, `hourly_rate`
5. `tutor_subject`: `tutor_subject_id`, `tutor_profile_id`, `subject_id`
6. `availability`: `availability_id`, `tutor_profile_id`, `day_of_week`, `start_time`, `end_time`
7. `booking`: `booking_id`, `student_id`, `tutor_profile_id`, `availability_id`, `session_date`, `status`
8. `review`: `review_id`, `booking_id`, `rating`, `comment`

The schema file includes primary keys, foreign keys, uniqueness rules and basic checks. Confirm all constraints against the lecturer's ERD before production use.

## Current implementation status

- [x] Individual development branch created: `mentorme-development`
- [x] Maven / Spring Boot project descriptor
- [x] Application entry point
- [x] MySQL schema for the eight ERD tables
- [x] Eight JPA entity classes
- [x] Eight Spring Data repository interfaces
- [x] Basic landing page and stylesheet
- [x] Initial development security configuration (public access only; not production-ready)
- [ ] Database-backed registration and login
- [ ] Role-based Student, Tutor and Admin authorization
- [ ] Admin faculty and subject management
- [ ] Tutor profile, subjects and availability management
- [ ] Student tutor search and booking
- [ ] Booking conflict handling and status workflow
- [ ] Reviews
- [ ] Automated tests and security verification
- [ ] Production environment variables, cloud MySQL and deployment

## Local setup (when using the home laptop)

1. Install JDK 21, Apache NetBeans, MySQL Server and Git.
2. Clone this repository and open the `MentorMe` folder as a Maven project in NetBeans.
3. Run `database/schema.sql` in MySQL Workbench.
4. Set the environment variables `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` for the local MySQL instance.
5. Run the Spring Boot main class `com.mentorme.MentorMeApplication`.
6. Open `http://localhost:8080`.

The application has not yet been built or tested in a local Java runtime. Treat the above as the setup target; verify each step on the home laptop.

## Security rules

- Never commit real database credentials, API keys or secrets.
- Passwords must be stored using a strong one-way password hash (BCrypt or equivalent).
- Public registration must never allow a user to self-assign the Admin role.
- Enforce authorization on the server, not only by hiding interface links.
- Booking creation must validate the selected tutor, availability, date and slot conflict in a database transaction.
- The current permissive development security configuration must be replaced before public deployment.

## Development conventions

Use small, meaningful commits. Keep the final ERD as the source of truth. Document changes to database structure and test each feature before moving to the next stage.
