package com.mentorme.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "availability")
public class Availability {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id")
    private Long availabilityId;
    @Column(name = "tutor_profile_id", nullable = false)
    private Long tutorProfileId;
    @Column(name = "day_of_week", nullable = false, length = 15)
    private String dayOfWeek;
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    protected Availability() {}
    public Long getAvailabilityId() { return availabilityId; }
    public Long getTutorProfileId() { return tutorProfileId; }
    public void setTutorProfileId(Long tutorProfileId) { this.tutorProfileId = tutorProfileId; }
    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
}