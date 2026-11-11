# MentorMe development status

## Completed in the personal GitHub branch

- Personal branch `mentorme-development`
- Spring Boot Maven project structure
- MySQL schema for the eight tables in the final ERD
- Eight JPA entity classes
- Eight Spring Data repository interfaces
- Landing page controller, Thymeleaf page and responsive stylesheet
- Initial GitHub Actions Maven workflow

## Not complete yet

This is an initial project scaffold, not a finished or deployable application.

- The project has not been compiled or run in a local NetBeans/JDK environment.
- Database connectivity has not been verified.
- Registration, login and logout are not implemented.
- The current development security configuration permits all requests and MUST be replaced before deployment.
- Role-specific dashboards and CRUD operations are not implemented.
- Booking conflict logic and review eligibility checks are not implemented.
- Cloud database and production hosting are not configured.

## Next development sequence

1. Clone the personal branch on the home laptop.
2. Verify JDK 21 and Maven build.
3. Run the MySQL schema and confirm all eight tables.
4. Start the app and resolve any compile or mapping errors.
5. Implement database-backed authentication and server-side role authorization.
6. Implement Admin, Tutor and Student features in small increments.
7. Add tests for validation, authorization, booking conflicts and reviews.
8. Configure managed MySQL and Java-compatible application hosting.
9. Deploy only after security and integration tests pass.
