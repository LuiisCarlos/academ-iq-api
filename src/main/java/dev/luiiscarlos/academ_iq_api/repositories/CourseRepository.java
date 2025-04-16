package dev.luiiscarlos.academ_iq_api.repositories;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCategoryName(String category);

    List<Course> findByName(String name);

    List<Course> findByDuration(LocalTime duration);

    List<Course> findByCreatedAt(LocalDateTime createdAt);

    Boolean existsByName(String name);

}
