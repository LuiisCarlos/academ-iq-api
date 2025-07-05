package dev.luiiscarlos.academ_iq_api.features.learning.course.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;

public interface CourseService {

    /**
     * Find all courses and return their public information
     *
     * @return a list of {@link CoursePublicResponse} containing the public info
     *         of all courses
     */
    Page<CoursePublicResponse> getAllPublic(Pageable pageable);

    /**
     * Finds the course by its id
     *
     * @param courseId the ID of the course
     * @return {@link CourseResponse} the course details
     */
    CourseResponse get(long courseId);

}