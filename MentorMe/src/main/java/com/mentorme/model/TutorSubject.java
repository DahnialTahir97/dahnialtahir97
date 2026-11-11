package com.mentorme.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tutor_subject",
       uniqueConstraints = @UniqueConstraint(name = "uq_tutor_subject_pair",
           columnNames = {"tutor_profile_id", "subject_id"}))
public class TutorSubject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tutor_subject_id")
    private Long tutorSubjectId;
    @Column(name = "tutor_profile_id", nullable = false)
    private Long tutorProfileId;
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;
    protected TutorSubject() {}
    public Long getTutorSubjectId() { return tutorSubjectId; }
    public Long getTutorProfileId() { return tutorProfileId; }
    public void setTutorProfileId(Long tutorProfileId) { this.tutorProfileId = tutorProfileId; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
}