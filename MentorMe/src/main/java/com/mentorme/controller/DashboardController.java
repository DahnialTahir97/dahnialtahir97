package com.mentorme.controller;

import com.mentorme.model.TutorProfile;
import com.mentorme.repository.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {
    private final UserAccountRepository users;
    private final FacultyRepository faculties;
    private final SubjectRepository subjects;
    private final TutorProfileRepository tutors;
    private final BookingRepository bookings;
    private final JdbcTemplate jdbc;

    public DashboardController(UserAccountRepository users, FacultyRepository faculties,
                               SubjectRepository subjects, TutorProfileRepository tutors,
                               BookingRepository bookings, JdbcTemplate jdbc) {
        this.users = users;
        this.faculties = faculties;
        this.subjects = subjects;
        this.tutors = tutors;
        this.bookings = bookings;
        this.jdbc = jdbc;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("userCount", users.count());
        model.addAttribute("facultyCount", faculties.count());
        model.addAttribute("subjectCount", subjects.count());
        model.addAttribute("bookingCount", bookings.count());
        model.addAttribute("recentBookings", jdbc.queryForList(
                "SELECT b.booking_id, u.full_name AS student_name, tu.full_name AS tutor_name, " +
                "b.session_date, b.status FROM booking b " +
                "JOIN users u ON u.user_id = b.student_id " +
                "JOIN tutor_profile tp ON tp.tutor_profile_id = b.tutor_profile_id " +
                "JOIN users tu ON tu.user_id = tp.user_id ORDER BY b.session_date DESC LIMIT 8"));
        return "admin-dashboard";
    }

    @GetMapping("/tutor")
    public String tutor(Authentication authentication, Model model) {
        users.findByEmailIgnoreCase(authentication.getName()).ifPresent(user -> {
            tutors.findByUserId(user.getUserId()).ifPresent(profile -> {
                model.addAttribute("profile", profile);
                model.addAttribute("tutorBookings", jdbc.queryForList(
                        "SELECT b.booking_id, b.availability_id, su.full_name AS student_name, b.session_date, b.status " +
                        "FROM booking b JOIN users su ON su.user_id = b.student_id " +
                        "WHERE b.tutor_profile_id = ? ORDER BY b.session_date DESC",
                        profile.getTutorProfileId()));
                model.addAttribute("tutorSubjects", jdbc.queryForList(
                        "SELECT s.subject_id, s.subject_code, s.subject_name FROM tutor_subject ts " +
                        "JOIN subject s ON s.subject_id = ts.subject_id WHERE ts.tutor_profile_id = ? ORDER BY s.subject_code",
                        profile.getTutorProfileId()));
                model.addAttribute("allSubjects", jdbc.queryForList("SELECT subject_id, subject_code, subject_name FROM subject ORDER BY subject_code"));
                model.addAttribute("tutorAvailability", jdbc.queryForList(
                        "SELECT availability_id, day_of_week, start_time, end_time FROM availability " +
                        "WHERE tutor_profile_id = ? ORDER BY day_of_week, start_time",
                        profile.getTutorProfileId()));
            });
        });
        return "tutor-dashboard";
    }

    @GetMapping("/student")
    public String student(Authentication authentication, Model model) {
        model.addAttribute("subjects", subjects.findAll());
        model.addAttribute("tutorDirectory", jdbc.queryForList(
                "SELECT tp.tutor_profile_id, u.full_name, f.name AS faculty_name, tp.bio, tp.hourly_rate, " +
                "GROUP_CONCAT(DISTINCT s.subject_code ORDER BY s.subject_code SEPARATOR ', ') AS subject_codes " +
                "FROM tutor_profile tp JOIN users u ON u.user_id = tp.user_id " +
                "JOIN faculty f ON f.faculty_id = u.faculty_id " +
                "LEFT JOIN tutor_subject ts ON ts.tutor_profile_id = tp.tutor_profile_id " +
                "LEFT JOIN subject s ON s.subject_id = ts.subject_id " +
                "GROUP BY tp.tutor_profile_id, u.full_name, f.name, tp.bio, tp.hourly_rate " +
                "ORDER BY u.full_name"));
        model.addAttribute("tutorSlots", jdbc.queryForList(
                "SELECT availability_id, tutor_profile_id, day_of_week, start_time, end_time " +
                "FROM availability ORDER BY day_of_week, start_time"));
        users.findByEmailIgnoreCase(authentication.getName()).ifPresent(user ->
                model.addAttribute("myBookings", jdbc.queryForList(
                        "SELECT b.booking_id, tu.full_name AS tutor_name, b.session_date, b.status, " +
                        "EXISTS (SELECT 1 FROM review r WHERE r.booking_id = b.booking_id) AS reviewed " +
                        "FROM booking b JOIN tutor_profile tp ON tp.tutor_profile_id = b.tutor_profile_id " +
                        "JOIN users tu ON tu.user_id = tp.user_id WHERE b.student_id = ? " +
                        "ORDER BY b.session_date DESC", user.getUserId())));
        return "student-dashboard";
    }
}
