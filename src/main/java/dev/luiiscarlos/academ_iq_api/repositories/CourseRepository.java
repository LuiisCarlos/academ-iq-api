package dev.luiiscarlos.academ_iq_api.repositories;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTitle(String title);

    List<Course> findByDuration(LocalTime duration);

    List<Course> findByCreatedAt(LocalDateTime createdAt);

    Boolean existsByTitle(String title);

    @Query("SELECT l.id FROM Course c JOIN c.sections s JOIN s.lessons l WHERE c.id = :courseId")
    List<Long> findAllLessonIdsById(@Param("courseId") Long courseId);

}
