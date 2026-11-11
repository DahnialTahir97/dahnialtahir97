package com.mentorme.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tutor_profile")
public class TutorProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tutor_profile_id")
    private Long tutorProfileId;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;
    @Column(name = "hourly_rate", nullable = false, precision = 8, scale = 2)
    private BigDecimal hourlyRate = BigDecimal.ZERO;
    protected TutorProfile() {}
    public Long getTutorProfileId() { return tutorProfileId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }
}