package com.mentorme.service;
import com.mentorme.model.UserAccount;
import com.mentorme.repository.FacultyRepository;
import com.mentorme.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class RegistrationService {
 private final UserAccountRepository users; private final FacultyRepository faculties; private final PasswordEncoder encoder;
 public RegistrationService(UserAccountRepository u,FacultyRepository f,PasswordEncoder e){users=u;faculties=f;encoder=e;}
 @Transactional public void register(String name,String email,String rawPassword,String role,Long facultyId){
  if(users.existsByEmailIgnoreCase(email))throw new IllegalArgumentException("Email is already registered.");
  if(!faculties.existsById(facultyId))throw new IllegalArgumentException("Select a valid faculty.");
  if(!"STUDENT".equals(role)&&!"TUTOR".equals(role))throw new IllegalArgumentException("Invalid role.");
  UserAccount u=new UserAccount();u.setFullName(name.trim());u.setEmail(email.trim().toLowerCase());u.setPassword(encoder.encode(rawPassword));u.setRole(role);u.setFacultyId(facultyId);users.save(u);
 }
}