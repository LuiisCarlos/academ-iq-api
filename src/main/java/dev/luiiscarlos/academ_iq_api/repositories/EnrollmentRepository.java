package dev.luiiscarlos.academ_iq_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.Enrollment;

import java.util.List;
import java.util.Optional;


@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId")
    List<Enrollment> findAllByUserId(@Param("userId") Long userId);

    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);

    void deleteByUserIdAndCourseId(Long userId, Long courseId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    /* @Modifying
    @Query("UPDATE Enrollment e SET e.progress = :progress WHERE e.id = :enrollmentId")
    void updateProgressById(@Param("enrollmentId") Long enrollmentId, @Param("progress") String progress); */

}
