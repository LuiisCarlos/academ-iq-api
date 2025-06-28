package dev.luiiscarlos.academ_iq_api.features.learning.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @NonNull
    @EntityGraph(attributePaths = {
            "category", "category.benefits",
            "reviews",
            "requirements",
            "sections", "sections.lessons",
            "instructor", "instructor.avatar",
            "thumbnail" })
    Page<Course> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {
            "category", "category.benefits",
            "reviews",
            "requirements",
            "sections", "sections.lessons",
            "instructor", "instructor.avatar",
            "thumbnail" })
    Optional<Course> findById(long id);

    Boolean existsByTitle(String title);

    @Query("SELECT l.id FROM Course c JOIN c.sections s JOIN s.lessons l WHERE c.id = :id")
    List<Long> findAllLessonIdsById(@Param("id") long courseId);

}
