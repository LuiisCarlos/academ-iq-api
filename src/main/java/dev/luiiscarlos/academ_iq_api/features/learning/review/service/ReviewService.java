package dev.luiiscarlos.academ_iq_api.features.learning.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.review.exception.ReviewAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.learning.review.exception.ReviewNotFoundException;

public interface ReviewService {
    /**
     * Saves a rating for a course by a user
     *
     * @param userId   the ID of the user
     * @param courseId the ID of the course to be rated
     * @param request  {@link ReviewRequest} the info of the review to be saved
     * @return {@link ReviewResponse} The saved review response dto
     * @throws ReviewAlreadyExistsException if the user has already rated the course
     */
    ReviewResponse create(long userId, ReviewRequest request);

    /**
     *  Retrieves all the reviews for a specified course by its ID
     *
     * @param pageable  the pageable options
     * @param courseId  the ID of the course
     * @return a pageable list of {@link ReviewResponse}
     */
    Page<ReviewResponse> getAll(Pageable pageable, long courseId);

    /**
     * Gets the review by its relationship with the user and course
     *
     * @param userId   the ID of the user
     * @param courseId the ID of the course to be rated
     * @return {@link ReviewResponse} The updated rating response dto
     * @throws ReviewNotFoundException if the user has not rated the course
     */
    ReviewResponse get(long userId, long courseId);

}
