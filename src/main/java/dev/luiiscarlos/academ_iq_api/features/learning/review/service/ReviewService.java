package dev.luiiscarlos.academ_iq_api.features.learning.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.review.exception.ReviewAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.learning.review.exception.ReviewNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.review.mapper.ReviewMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.review.model.Review;
import dev.luiiscarlos.academ_iq_api.features.learning.review.repository.ReviewRepository;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    /**
     * Saves a rating for a course by a user
     *
     * @param userId   the ID of the user
     * @param courseId the ID of the course to be rated
     * @param request  {@link ReviewRequest} the info of the review to be saved
     * @return {@link ReviewResponse} The saved review response dto
     * @throws IllegalArgumentException if the user has already rated the course
     */
    public ReviewResponse create(long userId, ReviewRequest request) {
        if (reviewRepository.existsByUserIdAndCourseId(userId, request.getCourseId()))
            throw new ReviewAlreadyExistsException("Failed to create rating: User already rated");

        User user = new User();
        user.setId(userId);

        Course course = new Course();
        course.setId(request.getCourseId());

        Review review = reviewMapper.toEntity(request);
        review.setUser(user);
        review.setCourse(course);

        return reviewMapper.toDto(reviewRepository.save(review));
    }

    /**
     *
     */
    public Page<ReviewResponse> getAll(Pageable Pageable, long courseId) {
        Page<Review> reviews = reviewRepository.findAllByCourseId(Pageable, courseId);

        return reviews.map(r -> reviewMapper.toDto(r));
    }

    /**
     * Gets the review by its relationship with the user and course
     *
     * @param userId   the ID of the user
     * @param courseId the ID of the course to be rated
     * @return {@link ReviewResponse} The updated rating response dto
     * @throws ReviewNotFoundException if the user has not rated the course
     */
    public ReviewResponse get(long userId, long courseId) {
        Review review = reviewRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new ReviewNotFoundException("Failed to retrieve rating: User not found"));

        return reviewMapper.toDto(review);
    }

}
