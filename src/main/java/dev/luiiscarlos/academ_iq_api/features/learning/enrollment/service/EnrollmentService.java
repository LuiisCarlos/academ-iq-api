package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service;

import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;

import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.exception.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;

public interface EnrollmentService {

    /**
     * Creates a new enrollment for a user in a specific course
     *
     * @param userId   the ID current authenticated user
     * @param courseId the ID of the course
     * @param args     a map containing optional enrollment arguments
     * @return an {@link Enrollment} the newly created enrollment
     * @throws EnrollmentNotFoundException if the user is already enrolled in the
     *                                     course
     */
    EnrollmentResponse create(long userId, long courseId, @Nullable Map<String, Boolean> args);

    /**
     * Retrieves the list of enrollments for a specific user
     *
     * @param userId the ID of the authenticated user
     * @return an {@link EnrollmentResponse} containing enrollment data
     * @throws EnrollmentNotFoundException if no enrollments found for the given
     *                                     user
     */
    List<EnrollmentResponse> getAll(long userId);

    /**
     * Retrieves the enrollment information for a specific user and course
     *
     * @param userId   the ID of the authenticated user
     * @param courseId the ID of the course
     * @return an {@link EnrollmentResponse} containing enrollment data
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    EnrollmentResponse get(long userId, long courseId);

    /**
     * Retrieves the enrollment for a specific user and course, or creates a new one
     * if it doesn't exist
     *
     * @param userId   the ID of the authenticated user
     * @param courseId the ID of the course
     * @return an {@link Enrollment} the existing or newly created enrollment
     */
    Enrollment getOrCreate(long userId, long courseId);

    /**
     * Partially updates enrollment properties (isFavorite, isArchived, isCompleted)
     * for a specific user and course
     *
     * @param userId   the ID of the authenticated user
     * @param courseId the ID of the course
     * @param args     a map containing the fields to update and their new values
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    EnrollmentResponse update(long userId, long courseId, Map<String, Boolean> args);

    /**
     * Updates the progress state of a user's enrollment in a course
     *
     * @param userId   the ID of the authenticated user
     * @param courseId the ID of the course
     * @param args     a map containing the sectionId, lessonId, and isCompleted
     *                 status for the lesson
     * @return an {@link Enrollment} the updated enrollment with progress state
     */
    EnrollmentResponse patchProgress(long userId, long courseId, Map<String, Object> args);

    /**
     * Deletes the enrollment for a specific user and course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    void delete(long userId, long courseId);

}