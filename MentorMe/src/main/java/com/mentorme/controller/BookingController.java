package com.mentorme.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {
    private final JdbcTemplate jdbc;

    public BookingController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @PostMapping("/student/bookings")
    public String createBooking(@RequestParam Long tutorProfileId,
                                @RequestParam Long availabilityId,
                                @RequestParam String sessionDate,
                                Authentication authentication,
                                RedirectAttributes redirect) {
        try {
            LocalDate date = LocalDate.parse(sessionDate);
            if (!date.isAfter(LocalDate.now())) {
                redirect.addFlashAttribute("error", "Choose a future session date.");
                return "redirect:/student";
            }
            Long studentId = userId(authentication.getName());
            Map<String, Object> slot = jdbc.queryForMap(
                    "SELECT tutor_profile_id, day_of_week FROM availability WHERE availability_id = ? FOR UPDATE",
                    availabilityId);
            if (((Number) slot.get("tutor_profile_id")).longValue() != tutorProfileId) {
                redirect.addFlashAttribute("error", "The selected time slot does not belong to this tutor.");
                return "redirect:/student";
            }
            if (!date.getDayOfWeek().name().equals(slot.get("day_of_week"))) {
                redirect.addFlashAttribute("error", "The selected date does not match the tutor's available day.");
                return "redirect:/student";
            }
            Long tutorUserId = jdbc.queryForObject(
                    "SELECT user_id FROM tutor_profile WHERE tutor_profile_id = ?", Long.class, tutorProfileId);
            if (tutorUserId != null && tutorUserId.equals(studentId)) {
                redirect.addFlashAttribute("error", "You cannot book your own tutor profile.");
                return "redirect:/student";
            }
            Integer conflicts = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM booking WHERE availability_id = ? AND session_date = ? AND status IN ('PENDING','APPROVED')",
                    Integer.class, availabilityId, date);
            if (conflicts != null && conflicts > 0) {
                redirect.addFlashAttribute("error", "This time slot has already been requested or booked.");
                return "redirect:/student";
            }
            jdbc.update("INSERT INTO booking (student_id, tutor_profile_id, availability_id, session_date, status) VALUES (?, ?, ?, ?, 'PENDING')",
                    studentId, tutorProfileId, availabilityId, date);
            redirect.addFlashAttribute("success", "Booking request sent. You can track it under My bookings.");
        } catch (DateTimeParseException | org.springframework.dao.EmptyResultDataAccessException ex) {
            redirect.addFlashAttribute("error", "The selected tutor or availability could not be found.");
        } catch (IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/student";
    }

    @Transactional
    @PostMapping("/student/bookings/cancel")
    public String cancelStudentBooking(@RequestParam Long bookingId, Authentication authentication,
                                       RedirectAttributes redirect) {
        Long studentId = userId(authentication.getName());
        int changed = jdbc.update(
                "UPDATE booking SET status = 'CANCELLED' WHERE booking_id = ? AND student_id = ? AND status = 'PENDING'",
                bookingId, studentId);
        redirect.addFlashAttribute(changed == 1 ? "success" : "error",
                changed == 1 ? "Booking request cancelled." : "Only your pending booking can be cancelled.");
        return "redirect:/student";
    }

    @Transactional
    @PostMapping("/tutor/bookings/status")
    public String updateTutorBooking(@RequestParam Long bookingId, @RequestParam String status,
                                     Authentication authentication, RedirectAttributes redirect) {
        if (!List.of("APPROVED", "COMPLETED", "CANCELLED").contains(status)) {
            redirect.addFlashAttribute("error", "Invalid booking status.");
            return "redirect:/tutor";
        }
        Long userId = userId(authentication.getName());
        String current;
        try {
            current = jdbc.queryForObject(
                    "SELECT b.status FROM booking b JOIN tutor_profile tp ON tp.tutor_profile_id = b.tutor_profile_id " +
                    "WHERE b.booking_id = ? AND tp.user_id = ? FOR UPDATE", String.class, bookingId, userId);
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            redirect.addFlashAttribute("error", "Booking not found for your tutor account.");
            return "redirect:/tutor";
        }
        boolean allowed = ("APPROVED".equals(status) && "PENDING".equals(current))
                || ("COMPLETED".equals(status) && "APPROVED".equals(current))
                || ("CANCELLED".equals(status) && ("PENDING".equals(current) || "APPROVED".equals(current)));
        if (!allowed) {
            redirect.addFlashAttribute("error", "This booking status change is not allowed.");
            return "redirect:/tutor";
        }
        jdbc.update("UPDATE booking SET status = ? WHERE booking_id = ? AND status = ?", status, bookingId, current);
        redirect.addFlashAttribute("success", "Booking status updated to " + status + ".");
        return "redirect:/tutor";
    }

    private Long userId(String email) {
        return jdbc.queryForObject("SELECT user_id FROM users WHERE email = ?", Long.class, email);
    }
}
