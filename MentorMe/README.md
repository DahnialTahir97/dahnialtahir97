# MentorMe — Individual Development Workspace

**Project:** Tutor-Match: Academic Peer Mentoring  
**Developer:** DahnialTahir97  
**Implementation:** Java web application (Spring Boot), Maven, Thymeleaf, MySQL  
**Database reference:** Final ERD supplied for this individual project.

## Purpose

This repository path is the developer's personal workspace for building an individual implementation of MentorMe. It is separate from the group repository.

## Authoritative database design

Use the final ERD supplied for this project. It contains these eight tables:

1. `faculty`
2. `users`
3. `subject`
4. `tutor_profile`
5. `tutor_subject`
6. `availability`
7. `booking`
8. `review`

Do not substitute the earlier ERD or rename columns without first reconciling the change with the final ERD.

## Planned technology

- Java 21 (target; verify installed JDK before development)
- Spring Boot
- Maven
- Thymeleaf, HTML, CSS and JavaScript
- MySQL
- Spring Data JPA / JDBC
- Git and GitHub
- Deployment target: a Java-compatible host such as Render; MySQL hosted separately

## Planned development stages

- [ ] Verify Java JDK and NetBeans installation
- [ ] Create the Spring Boot Maven application
- [ ] Implement the eight-table MySQL schema from the final ERD
- [ ] Connect the application to MySQL
- [ ] Implement authentication and role-based access (Student, Tutor, Admin)
- [ ] Implement faculty and subject management
- [ ] Implement tutor profile, tutor-subject mapping and hourly rate
- [ ] Implement tutor availability
- [ ] Implement student search and booking
- [ ] Implement booking status management
- [ ] Implement reviews
- [ ] Test validation, authorization, booking conflicts and security
- [ ] Configure production database and deployment
- [ ] Document setup and demonstration steps

## Individual development practice

- Develop and commit work under this personal repository.
- Keep credentials, database passwords, API keys and environment files out of Git.
- Use clear, incremental commits.
- Record design decisions and testing results as work progresses.
- Treat any group repository as a reference only; do not push individual work to it.

## Current status

Workspace initialized. Application code and database schema have not yet been implemented.
