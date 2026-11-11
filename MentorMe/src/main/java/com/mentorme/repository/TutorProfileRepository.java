package com.mentorme.repository;
import com.mentorme.model.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TutorProfileRepository extends JpaRepository<TutorProfile, Long> {
    Optional<TutorProfile> findByUserId(Long userId);
}