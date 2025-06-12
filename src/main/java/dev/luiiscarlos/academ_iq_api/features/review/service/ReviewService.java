package dev.luiiscarlos.academ_iq_api.features.review.service;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.features.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.features.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.review.exception.ReviewAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.review.exception.ReviewNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.review.mapper.ReviewMapper;
import dev.luiiscarlos.academ_iq_api.features.review.model.Review;
import dev.luiiscarlos.academ_iq_api.features.review.repository.ReviewRepository;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.user.service.impl.UserServiceImpl;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final UserServiceImpl userService;

    private final CourseService courseService;

    /**
     * Saves a rating for a course by a user
     *
     * @param token      The authentication token of the user
     * @param courseId   The ID of the course to be rated
     * @param requestDto {@link ReviewRequest} The rating request data transfer
     *                   object containing the rating details
     * @return {@link ReviewResponse } The saved rating response data transfer
     *         object
     * @throws IllegalArgumentException if the user has already rated the course
     */
    public ReviewResponse save(String token, Long courseId, ReviewRequest request) {
        User user = userService.findByToken(token);
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
     * Updates a rating for a course by a user
     *
     * @param token      The authentication token of the user
     * @param courseId   The ID of the course to be rated
     * @param requestDto {@link ReviewRequest} The rating request data transfer
     *                   object containing the updated rating details
     * @return {@link ReviewResponse } The updated rating response data transfer
     *         object
     * @throws ReviewNotFoundException if the user has not rated the course
     */
    public ReviewResponse findByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        Review review = reviewRepository.findByUserIdAndCourseId(user.getId(), course.getId())
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Failed to retrieve rating: User not found"));

        return reviewMapper.toResponseDto(review);
    }

}
