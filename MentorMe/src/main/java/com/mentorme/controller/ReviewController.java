package com.mentorme.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReviewController {
    private final JdbcTemplate jdbc;

    public ReviewController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @PostMapping("/student/reviews")
    public String submitReview(@RequestParam Long bookingId,
                               @RequestParam Integer rating,
                               @RequestParam(required = false) String comment,
                               Authentication authentication,
                               RedirectAttributes redirect) {
        if (rating == null || rating < 1 || rating > 5) {
            redirect.addFlashAttribute("error", "Please select a rating from 1 to 5.");
            return "redirect:/student";
        }
        Long studentId = jdbc.queryForObject(
                "SELECT user_id FROM users WHERE email = ?", Long.class, authentication.getName());
        Integer eligible = jdbc.queryForObject(
                "SELECT COUNT(*) FROM booking WHERE booking_id = ? AND student_id = ? AND status = 'COMPLETED'",
                Integer.class, bookingId, studentId);
        if (eligible == null || eligible != 1) {
            redirect.addFlashAttribute("error", "Only the student who completed the booking can submit a review.");
            return "redirect:/student";
        }
        Integer alreadyReviewed = jdbc.queryForObject(
                "SELECT COUNT(*) FROM review WHERE booking_id = ?", Integer.class, bookingId);
        if (alreadyReviewed != null && alreadyReviewed > 0) {
            redirect.addFlashAttribute("error", "A review has already been submitted for this session.");
            return "redirect:/student";
        }
        String cleanComment = comment == null ? "" : comment.trim();
        if (cleanComment.length() > 2000) {
            redirect.addFlashAttribute("error", "Review comment must not exceed 2,000 characters.");
            return "redirect:/student";
        }
        jdbc.update("INSERT INTO review (booking_id, rating, comment) VALUES (?, ?, ?)",
                bookingId, rating, cleanComment.isBlank() ? null : cleanComment);
        redirect.addFlashAttribute("success", "Thank you. Your review has been submitted.");
        return "redirect:/student";
    }
}
