package dev.luiiscarlos.academ_iq_api.features.learning.course.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;

public interface CourseCrudService {

    /**
     * Saves a new course or updates an existing course in the database
     *
     * @param course The course entity to save
     * @return The saved course entity
     */
    Course save(Course course);

    /**
     * Retrieves a paginated list of all courses from the database
     *
     * @param pageable Pagination information
     * @return A page of courses
     */
    Page<Course> findAll(Pageable pageable);

    /**
     * Finds a course by its unique ID from the database
     *
     * @param id The course ID
     * @return The course entity, or null if not found
     */
    Course findById(long id);

    /**
     * Retrieves all lesson IDs associated with a course by course ID
     *
     * @param id The course ID
     * @return List of lesson IDs
     */
    List<Long> findAllLessonIdsById(long id);

    /**
     * Deletes a course from the database by its unique ID
     *
     * @param id The course ID
     */
    void deleteById(long id);

    /**
     * Checks if a course exists in the database by its unique ID
     *
     * @param id The course ID
     * @return True if the course exists, false otherwise
     */
    boolean existsById(long id);

    /**
     * Checks if a course exists in the database by its title
     *
     * @param title The course title
     * @return True if the course exists, false otherwise
     */
    boolean existsByTitle(String title);

}
