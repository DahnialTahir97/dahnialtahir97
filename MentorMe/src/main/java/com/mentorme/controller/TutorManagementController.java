package com.mentorme.controller;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TutorManagementController {
    private final JdbcTemplate jdbc;

    public TutorManagementController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @PostMapping("/tutor/profile")
    public String updateProfile(@RequestParam String bio, @RequestParam BigDecimal hourlyRate,
                                Authentication authentication, RedirectAttributes redirect) {
        String cleanBio = bio == null ? "" : bio.trim();
        if (cleanBio.length() > 2000 || hourlyRate == null
                || hourlyRate.compareTo(BigDecimal.ZERO) < 0
                || hourlyRate.compareTo(new BigDecimal("999999.99")) > 0) {
            redirect.addFlashAttribute("error", "Enter a valid profile description and hourly rate.");
            return "redirect:/tutor";
        }
        Long userId = userId(authentication.getName());
        int updated = jdbc.update(
                "UPDATE tutor_profile SET bio = ?, hourly_rate = ? WHERE user_id = ?",
                cleanBio, hourlyRate, userId);
        redirect.addFlashAttribute(updated == 1 ? "success" : "error",
                updated == 1 ? "Tutor profile updated." : "Tutor profile could not be found.");
        return "redirect:/tutor";
    }

    @Transactional
    @PostMapping("/tutor/availability")
    public String addAvailability(@RequestParam String dayOfWeek,
                                  @RequestParam String startTime,
                                  @RequestParam String endTime,
                                  Authentication authentication,
                                  RedirectAttributes redirect) {
        try {
            DayOfWeek.valueOf(dayOfWeek);
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            if (!end.isAfter(start)) {
                redirect.addFlashAttribute("error", "End time must be later than start time.");
                return "redirect:/tutor";
            }
            Long profileId = profileId(authentication.getName());
            Integer overlap = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM availability WHERE tutor_profile_id = ? AND day_of_week = ? " +
                    "AND start_time < ? AND end_time > ?", Integer.class, profileId, dayOfWeek, end, start);
            if (overlap != null && overlap > 0) {
                redirect.addFlashAttribute("error", "This time overlaps an existing availability slot.");
                return "redirect:/tutor";
            }
            jdbc.update("INSERT INTO availability (tutor_profile_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)",
                    profileId, dayOfWeek, start, end);
            redirect.addFlashAttribute("success", "Availability added.");
        } catch (RuntimeException ex) {
            redirect.addFlashAttribute("error", "Please enter a valid day and time.");
        }
        return "redirect:/tutor";
    }

    @Transactional
    @PostMapping("/tutor/availability/delete")
    public String deleteAvailability(@RequestParam Long availabilityId,
                                     Authentication authentication,
                                     RedirectAttributes redirect) {
        Long profileId = profileId(authentication.getName());
        Integer linkedBookings = jdbc.queryForObject(
                "SELECT COUNT(*) FROM booking WHERE availability_id = ?", Integer.class, availabilityId);
        if (linkedBookings != null && linkedBookings > 0) {
            redirect.addFlashAttribute("error", "This slot has booking history and cannot be deleted.");
            return "redirect:/tutor";
        }
        int deleted = jdbc.update(
                "DELETE FROM availability WHERE availability_id = ? AND tutor_profile_id = ?",
                availabilityId, profileId);
        redirect.addFlashAttribute(deleted == 1 ? "success" : "error",
                deleted == 1 ? "Availability removed." : "Availability slot not found.");
        return "redirect:/tutor";
    }

    @Transactional
    @PostMapping("/tutor/subjects")
    public String addSubject(@RequestParam Long subjectId, Authentication authentication,
                             RedirectAttributes redirect) {
        Long userId = userId(authentication.getName());
        Long profileId = profileId(authentication.getName());
        Long userFaculty = jdbc.queryForObject("SELECT faculty_id FROM users WHERE user_id = ?", Long.class, userId);
        Integer allowed = jdbc.queryForObject(
                "SELECT COUNT(*) FROM subject WHERE subject_id = ? AND faculty_id = ?",
                Integer.class, subjectId, userFaculty);
        if (allowed == null || allowed != 1) {
            redirect.addFlashAttribute("error", "Choose a subject from your registered faculty.");
            return "redirect:/tutor";
        }
        jdbc.update("INSERT IGNORE INTO tutor_subject (tutor_profile_id, subject_id) VALUES (?, ?)",
                profileId, subjectId);
        redirect.addFlashAttribute("success", "Teaching subject saved.");
        return "redirect:/tutor";
    }

    @Transactional
    @PostMapping("/tutor/subjects/delete")
    public String removeSubject(@RequestParam Long subjectId, Authentication authentication,
                                RedirectAttributes redirect) {
        int deleted = jdbc.update(
                "DELETE FROM tutor_subject WHERE subject_id = ? AND tutor_profile_id = ?",
                subjectId, profileId(authentication.getName()));
        redirect.addFlashAttribute(deleted == 1 ? "success" : "error",
                deleted == 1 ? "Teaching subject removed." : "Subject was not assigned to your profile.");
        return "redirect:/tutor";
    }

    private Long userId(String email) {
        return jdbc.queryForObject("SELECT user_id FROM users WHERE email = ?", Long.class, email);
    }

    private Long profileId(String email) {
        return jdbc.queryForObject("SELECT tutor_profile_id FROM tutor_profile WHERE user_id = ?",
                Long.class, userId(email));
    }
}
