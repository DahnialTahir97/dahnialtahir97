package com.mentorme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "role", nullable = false, length = 20)
    private String role;
    @Column(name = "faculty_id", nullable = false)
    private Long facultyId;
    protected UserAccount() {}
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
}