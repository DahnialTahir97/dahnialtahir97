package com.mentorme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "subject")
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;
    @Column(name = "subject_code", nullable = false, length = 20)
    private String subjectCode;
    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;
    @Column(name = "faculty_id", nullable = false)
    private Long facultyId;
    protected Subject() {}
    public Long getSubjectId() { return subjectId; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
}