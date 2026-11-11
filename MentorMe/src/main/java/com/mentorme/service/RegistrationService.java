package com.mentorme.service;

import com.mentorme.model.TutorProfile;
import com.mentorme.model.UserAccount;
import com.mentorme.repository.FacultyRepository;
import com.mentorme.repository.TutorProfileRepository;
import com.mentorme.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final UserAccountRepository users;
    private final FacultyRepository faculties;
    private final TutorProfileRepository tutorProfiles;
    private final PasswordEncoder encoder;

    public RegistrationService(UserAccountRepository users, FacultyRepository faculties,
                               TutorProfileRepository tutorProfiles, PasswordEncoder encoder) {
        this.users = users;
        this.faculties = faculties;
        this.tutorProfiles = tutorProfiles;
        this.encoder = encoder;
    }

    @Transactional
    public void register(String name, String email, String rawPassword, String role, Long facultyId) {
        String cleanName = name == null ? "" : name.trim();
        String cleanEmail = email == null ? "" : email.trim().toLowerCase();
        if (cleanName.isBlank() || cleanName.length() > 100) {
            throw new IllegalArgumentException("Enter a valid name (maximum 100 characters).");
        }
        if (!cleanEmail.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || cleanEmail.length() > 100) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        if (rawPassword == null || rawPassword.length() < 8 || rawPassword.length() > 72) {
            throw new IllegalArgumentException("Password must contain 8 to 72 characters.");
        }
        if (users.existsByEmailIgnoreCase(cleanEmail)) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        if (facultyId == null || !faculties.existsById(facultyId)) {
            throw new IllegalArgumentException("Select a valid faculty.");
        }
        if (!"STUDENT".equals(role) && !"TUTOR".equals(role)) {
            throw new IllegalArgumentException("Invalid role.");
        }

        UserAccount user = new UserAccount();
        user.setFullName(cleanName);
        user.setEmail(cleanEmail);
        user.setPassword(encoder.encode(rawPassword));
        user.setRole(role);
        user.setFacultyId(facultyId);
        UserAccount saved = users.save(user);

        // Every tutor account receives its own profile immediately.
        if ("TUTOR".equals(role)) {
            TutorProfile profile = new TutorProfile();
            profile.setUserId(saved.getUserId());
            profile.setBio("New MentorMe tutor. Update your introduction and teaching subjects.");
            tutorProfiles.save(profile);
        }
    }
}
