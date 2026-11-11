package com.mentorme.repository;
import com.mentorme.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStudentId(Long studentId);
    List<Booking> findByTutorProfileId(Long tutorProfileId);
    boolean existsByAvailabilityIdAndSessionDateAndStatusIn(
        Long availabilityId, LocalDate sessionDate, List<String> statuses);
}