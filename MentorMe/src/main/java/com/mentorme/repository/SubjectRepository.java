package com.mentorme.repository;
import com.mentorme.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByFacultyId(Long facultyId);
}