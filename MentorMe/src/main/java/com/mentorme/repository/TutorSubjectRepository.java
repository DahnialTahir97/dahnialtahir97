package com.mentorme.repository;
import com.mentorme.model.TutorSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TutorSubjectRepository extends JpaRepository<TutorSubject, Long> {
    List<TutorSubject> findByTutorProfileId(Long tutorProfileId);
    boolean existsByTutorProfileIdAndSubjectId(Long tutorProfileId, Long subjectId);
}