package com.mentorme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty")
public class Faculty {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long facultyId;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    protected Faculty() {}
    public Long getFacultyId() { return facultyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}