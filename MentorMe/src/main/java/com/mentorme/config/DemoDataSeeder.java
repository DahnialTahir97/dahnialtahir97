package com.mentorme.config;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Optional, repeatable demo-data loader.
 * Enable with MENTORME_DEMO_DATA_ENABLED=true.
 * All names and records are fictional; passwords are encoded before storage.
 */
@Component
@ConditionalOnProperty(name = "mentorme.demo-data.enabled", havingValue = "true")
public class DemoDataSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(JdbcTemplate jdbc, PasswordEncoder passwordEncoder) {
        this.jdbc = jdbc;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        addFaculty("Faculty of Computer and Mathematical Sciences");
        addFaculty("Faculty of Business and Management");
        addFaculty("Faculty of Accountancy");
        addFaculty("Faculty of Information Management");

        long computerFaculty = facultyId("Faculty of Computer and Mathematical Sciences");
        long businessFaculty = facultyId("Faculty of Business and Management");
        long accountancyFaculty = facultyId("Faculty of Accountancy");

        addSubject("CSC510", "Introduction to Data Communication", computerFaculty);
        addSubject("CSC574", "Dynamic Web Application Development", computerFaculty);
        addSubject("CSC584", "Enterprise Programming", computerFaculty);
        addSubject("MGT420", "Principles and Practice of Management", businessFaculty);
        addSubject("FIN420", "Financial Management", businessFaculty);
        addSubject("ACC406", "Principles of Accounting", accountancyFaculty);

        addUser("Demo Administrator", "admin@mentorme.demo", "AdminDemo123!", "ADMIN", computerFaculty);
        addUser("Aisyah Rahman", "aisyah.tutor@mentorme.demo", "TutorDemo123!", "TUTOR", computerFaculty);
        addUser("Daniel Lee", "daniel.tutor@mentorme.demo", "TutorDemo123!", "TUTOR", businessFaculty);
        addUser("Nur Iman", "iman.student@mentorme.demo", "StudentDemo123!", "STUDENT", computerFaculty);
        addUser("Amir Hakim", "amir.student@mentorme.demo", "StudentDemo123!", "STUDENT", businessFaculty);
        addUser("Sofia Tan", "sofia.student@mentorme.demo", "StudentDemo123!", "STUDENT", accountancyFaculty);

        long aisyah = userId("aisyah.tutor@mentorme.demo");
        long daniel = userId("daniel.tutor@mentorme.demo");
        long iman = userId("iman.student@mentorme.demo");
        long amir = userId("amir.student@mentorme.demo");

        addTutorProfile(aisyah, "Computer science peer tutor. I enjoy explaining programming concepts through practical examples.", "25.00");
        addTutorProfile(daniel, "Business student who can help with management and finance fundamentals.", "20.00");

        long aisyahProfile = tutorProfileId(aisyah);
        long danielProfile = tutorProfileId(daniel);
        addTutorSubject(aisyahProfile, subjectId("CSC584"));
        addTutorSubject(aisyahProfile, subjectId("CSC574"));
        addTutorSubject(aisyahProfile, subjectId("CSC510"));
        addTutorSubject(danielProfile, subjectId("MGT420"));
        addTutorSubject(danielProfile, subjectId("FIN420"));

        addAvailability(aisyahProfile, "MONDAY", "10:00:00", "11:00:00");
        addAvailability(aisyahProfile, "WEDNESDAY", "14:00:00", "15:00:00");
        addAvailability(danielProfile, "TUESDAY", "10:00:00", "11:00:00");
        addAvailability(danielProfile, "THURSDAY", "14:00:00", "15:00:00");

        // Two illustrative historical bookings and one review; no real appointments exist.
        long completedSlot = addAvailabilityIfMissing(aisyahProfile, "FRIDAY", "09:00:00", "10:00:00");
        long completedBooking = addBooking(iman, aisyahProfile, completedSlot,
                LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY)).minusWeeks(1), "COMPLETED");
        if (completedBooking > 0) {
            jdbc.update("INSERT IGNORE INTO review (booking_id, rating, comment) VALUES (?, ?, ?)",
                    completedBooking, 5, "Clear explanation and helpful coding examples. (Demo review)");
        }
        long pendingSlot = addAvailabilityIfMissing(danielProfile, "SATURDAY", "11:00:00", "12:00:00");
        addBooking(amir, danielProfile, pendingSlot,
                LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).plusWeeks(1), "PENDING");
    }

    private void addFaculty(String name) {
        jdbc.update("INSERT IGNORE INTO faculty (name) VALUES (?)", name);
    }

    private long facultyId(String name) {
        return jdbc.queryForObject("SELECT faculty_id FROM faculty WHERE name = ?", Long.class, name);
    }

    private void addSubject(String code, String name, long facultyId) {
        jdbc.update("INSERT IGNORE INTO subject (subject_code, subject_name, faculty_id) VALUES (?, ?, ?)",
                code, name, facultyId);
    }

    private long subjectId(String code) {
        return jdbc.queryForObject("SELECT subject_id FROM subject WHERE subject_code = ?", Long.class, code);
    }

    private void addUser(String name, String email, String rawPassword, String role, long facultyId) {
        if (jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE email = ?", Integer.class, email) == 0) {
            jdbc.update("INSERT INTO users (full_name, email, password, role, faculty_id) VALUES (?, ?, ?, ?, ?)",
                    name, email, passwordEncoder.encode(rawPassword), role, facultyId);
        }
    }

    private long userId(String email) {
        return jdbc.queryForObject("SELECT user_id FROM users WHERE email = ?", Long.class, email);
    }

    private void addTutorProfile(long userId, String bio, String rate) {
        jdbc.update("INSERT IGNORE INTO tutor_profile (user_id, bio, hourly_rate) VALUES (?, ?, ?)",
                userId, bio, new BigDecimal(rate));
    }

    private long tutorProfileId(long userId) {
        return jdbc.queryForObject("SELECT tutor_profile_id FROM tutor_profile WHERE user_id = ?", Long.class, userId);
    }

    private void addTutorSubject(long profileId, long subjectId) {
        jdbc.update("INSERT IGNORE INTO tutor_subject (tutor_profile_id, subject_id) VALUES (?, ?)",
                profileId, subjectId);
    }

    private void addAvailability(long profileId, String day, String start, String end) {
        addAvailabilityIfMissing(profileId, day, start, end);
    }

    private long addAvailabilityIfMissing(long profileId, String day, String start, String end) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM availability WHERE tutor_profile_id = ? AND day_of_week = ? AND start_time = ? AND end_time = ?",
                Integer.class, profileId, day, start, end);
        if (count == null || count == 0) {
            jdbc.update("INSERT INTO availability (tutor_profile_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)",
                    profileId, day, start, end);
        }
        return jdbc.queryForObject(
                "SELECT availability_id FROM availability WHERE tutor_profile_id = ? AND day_of_week = ? AND start_time = ? AND end_time = ?",
                Long.class, profileId, day, start, end);
    }

    private long addBooking(long studentId, long profileId, long availabilityId, LocalDate date, String status) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM booking WHERE student_id = ? AND availability_id = ? AND session_date = ?",
                Integer.class, studentId, availabilityId, date);
        if (count == null || count == 0) {
            jdbc.update("INSERT INTO booking (student_id, tutor_profile_id, availability_id, session_date, status) VALUES (?, ?, ?, ?, ?)",
                    studentId, profileId, availabilityId, date, status);
        }
        return jdbc.queryForObject(
                "SELECT booking_id FROM booking WHERE student_id = ? AND availability_id = ? AND session_date = ?",
                Long.class, studentId, availabilityId, date);
    }
}
