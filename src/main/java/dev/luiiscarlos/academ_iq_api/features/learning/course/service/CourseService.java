package dev.luiiscarlos.academ_iq_api.features.learning.course.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CoursePublicResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.exception.CourseAlreadyExistsException;

public interface CourseService {

    /**
     * Creates a new course with the given request and files
     *
     * @param request {@link CourseRequest} the course details
     * @param files   the files to be saved
     * @return {@link CourseResponse} the saved course
     * @throws CourseAlreadyExistsException if the course already exists
     */
    CourseResponse create(CourseRequest request, Map<String, MultipartFile> files);

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

    /**
     * Finds all lesson IDs for the given course ID
     *
     * @param courseId the ID of the course
     * @return a list of lesson IDs for the course
     */
    List<Long> getLessonsIds(long courseId);

    /**
     * Updates the course by its ID with the given request and files
     *
     * @param courseId the ID of the course
     * @param request  {@link CourseRequest} the course details
     * @param files    the files to be saved
     * @return an {@link CourseResponse} containing the course details
     */
    CourseResponse update(long courseId, CourseRequest request, Map<String, MultipartFile> files);

    /**
     * Deletes the course by its id
     *
     * @param courseId the ID of the course
     */
    void delete(long courseId);

}