package com.mentorme.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminManagementController {
    private final JdbcTemplate jdbc;

    public AdminManagementController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    @PostMapping("/admin/faculties")
    public String addFaculty(@RequestParam String name, RedirectAttributes redirect) {
        String clean = name == null ? "" : name.trim();
        if (clean.isBlank() || clean.length() > 100) {
            redirect.addFlashAttribute("error", "Faculty name is required (maximum 100 characters).");
            return "redirect:/admin";
        }
        try {
            jdbc.update("INSERT INTO faculty (name) VALUES (?)", clean);
            redirect.addFlashAttribute("success", "Faculty added.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error", "This faculty already exists.");
        }
        return "redirect:/admin";
    }

    @Transactional
    @PostMapping("/admin/faculties/delete")
    public String deleteFaculty(@RequestParam Long facultyId, RedirectAttributes redirect) {
        try {
            int deleted = jdbc.update("DELETE FROM faculty WHERE faculty_id = ?", facultyId);
            redirect.addFlashAttribute(deleted == 1 ? "success" : "error",
                    deleted == 1 ? "Faculty deleted." : "Faculty not found.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error", "Faculty is in use by users or subjects and cannot be deleted.");
        }
        return "redirect:/admin";
    }

    @Transactional
    @PostMapping("/admin/subjects")
    public String addSubject(@RequestParam String subjectCode, @RequestParam String subjectName,
                             @RequestParam Long facultyId, RedirectAttributes redirect) {
        String code = subjectCode == null ? "" : subjectCode.trim().toUpperCase();
        String name = subjectName == null ? "" : subjectName.trim();
        if (code.isBlank() || code.length() > 20 || name.isBlank() || name.length() > 100
                || facultyId == null || !existsFaculty(facultyId)) {
            redirect.addFlashAttribute("error", "Enter a valid subject code, name and faculty.");
            return "redirect:/admin";
        }
        try {
            jdbc.update("INSERT INTO subject (subject_code, subject_name, faculty_id) VALUES (?, ?, ?)",
                    code, name, facultyId);
            redirect.addFlashAttribute("success", "Subject added.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error", "This subject code already exists.");
        }
        return "redirect:/admin";
    }

    @Transactional
    @PostMapping("/admin/subjects/delete")
    public String deleteSubject(@RequestParam Long subjectId, RedirectAttributes redirect) {
        try {
            int deleted = jdbc.update("DELETE FROM subject WHERE subject_id = ?", subjectId);
            redirect.addFlashAttribute(deleted == 1 ? "success" : "error",
                    deleted == 1 ? "Subject deleted." : "Subject not found.");
        } catch (DataIntegrityViolationException ex) {
            redirect.addFlashAttribute("error", "Subject is assigned to a tutor and cannot be deleted.");
        }
        return "redirect:/admin";
    }

    private boolean existsFaculty(Long facultyId) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM faculty WHERE faculty_id = ?",
                Integer.class, facultyId);
        return count != null && count == 1;
    }
}
