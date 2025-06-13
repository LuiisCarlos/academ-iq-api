package dev.luiiscarlos.academ_iq_api.features.course.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.course.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @NonNull
    @EntityGraph(attributePaths = {
            "category",
            "requirements",
    })
    List<Course> findAll();

    @NonNull
    @EntityGraph(attributePaths = {
            "category","category.benefits",
            "reviews",
            "requirements",
            "sections","sections.lessons",
            "instructor", "instructor.avatar",
            "thumbnail"})
    Optional<Course> findById(@NonNull Long id);

    List<Course> findByTitle(String title);

    List<Course> findByDuration(Duration duration);

    List<Course> findByCreatedAt(LocalDateTime createdAt);

    Boolean existsByTitle(String title);

    @Query("SELECT l.id FROM Course c JOIN c.sections s JOIN s.lessons l WHERE c.id = :courseId")
    List<Long> findAllLessonIdsById(@Param("courseId") Long courseId);

}
