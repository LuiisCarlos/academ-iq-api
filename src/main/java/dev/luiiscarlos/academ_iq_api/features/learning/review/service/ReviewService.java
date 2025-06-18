package dev.luiiscarlos.academ_iq_api.features.learning.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;
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

    private final CourseService courseService;

    /**
     * Saves a rating for a course by a user
     *
     * @param user     the current authenticated user
     * @param courseId the ID of the course to be rated
     * @param request  {@link ReviewRequest} the info of the review to be saved
     * @return {@link ReviewResponse } The saved review response dto
     * @throws IllegalArgumentException if the user has already rated the course
     */
    public ReviewResponse create(User user, Long courseId, ReviewRequest request) {
        Course course = courseService.findById(courseId);

        if (reviewRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new ReviewAlreadyExistsException("Failed to create rating: User already rated");

        Review review = reviewMapper.toModel(request);
        review.setUser(user);
        review.setCourse(course);

        Review response = reviewRepository.save(review);

        return reviewMapper.toResponseDto(response);
    }

    /**
     * Gets the review by its relationship with the user and course
     *
     * @param user     the current authenticated user
     * @param courseId the ID of the course to be rated
     * @return {@link ReviewResponse } The updated rating response dto
     * @throws ReviewNotFoundException if the user has not rated the course
     */
    public ReviewResponse get(User user, Long courseId) {
        Course course = courseService.findById(courseId);

        Review review = reviewRepository.findByUserIdAndCourseId(user.getId(), course.getId())
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Failed to retrieve rating: User not found"));

        return reviewMapper.toResponseDto(review);
    }

}
