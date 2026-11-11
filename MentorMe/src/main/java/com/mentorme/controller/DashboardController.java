package com.mentorme.controller;
import com.mentorme.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DashboardController {
 private final UserAccountRepository users;private final FacultyRepository faculties;private final SubjectRepository subjects;private final TutorProfileRepository tutors;private final BookingRepository bookings;
 public DashboardController(UserAccountRepository u,FacultyRepository f,SubjectRepository s,TutorProfileRepository t,BookingRepository b){users=u;faculties=f;subjects=s;tutors=t;bookings=b;}
 @GetMapping("/admin")public String admin(Model m){m.addAttribute("userCount",users.count());m.addAttribute("facultyCount",faculties.count());m.addAttribute("subjectCount",subjects.count());m.addAttribute("bookingCount",bookings.count());return "admin-dashboard";}
 @GetMapping("/tutor")public String tutor(){return "tutor-dashboard";}
 @GetMapping("/student")public String student(Model m){m.addAttribute("subjects",subjects.findAll());m.addAttribute("tutors",tutors.findAll());return "student-dashboard";}
}