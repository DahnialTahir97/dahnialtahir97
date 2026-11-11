package com.mentorme.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking",
       indexes = @Index(name = "idx_booking_slot_date", columnList = "availability_id, session_date"))
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;
    @Column(name = "student_id", nullable = false)
    private Long studentId;
    @Column(name = "tutor_profile_id", nullable = false)
    private Long tutorProfileId;
    @Column(name = "availability_id", nullable = false)
    private Long availabilityId;
    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";
    protected Booking() {}
    public Long getBookingId() { return bookingId; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getTutorProfileId() { return tutorProfileId; }
    public void setTutorProfileId(Long tutorProfileId) { this.tutorProfileId = tutorProfileId; }
    public Long getAvailabilityId() { return availabilityId; }
    public void setAvailabilityId(Long availabilityId) { this.availabilityId = availabilityId; }
    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}