package com.mentorme.repository;
import com.mentorme.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FacultyRepository extends JpaRepository<Faculty, Long> {}