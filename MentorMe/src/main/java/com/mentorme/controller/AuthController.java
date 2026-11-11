package com.mentorme.controller;
import com.mentorme.repository.FacultyRepository;
import com.mentorme.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class AuthController {
 private final FacultyRepository faculties;private final RegistrationService registration;
 public AuthController(FacultyRepository f,RegistrationService r){faculties=f;registration=r;}
 @GetMapping("/login")public String login(){return "login";}
 @GetMapping("/register")public String registerForm(Model m){m.addAttribute("faculties",faculties.findAll());return "register";}
 @PostMapping("/register")public String register(@RequestParam String fullName,@RequestParam String email,@RequestParam String password,@RequestParam String role,@RequestParam Long facultyId,Model m,RedirectAttributes redirect){
  if(fullName.isBlank()||fullName.length()>100||email.length()>100||password.length()<8||password.length()>72){m.addAttribute("error","Check your details. Password must contain at least 8 characters.");m.addAttribute("faculties",faculties.findAll());return "register";}
  try{registration.register(fullName,email,password,role,facultyId);redirect.addFlashAttribute("success","Account created. Please log in.");return "redirect:/login";}
  catch(IllegalArgumentException ex){m.addAttribute("error",ex.getMessage());m.addAttribute("faculties",faculties.findAll());return "register";}
 }
 @GetMapping("/dashboard")public String dashboard(Authentication a){
  if(a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_ADMIN")))return "redirect:/admin";
  if(a.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("ROLE_TUTOR")))return "redirect:/tutor";
  return "redirect:/student";
 }
}