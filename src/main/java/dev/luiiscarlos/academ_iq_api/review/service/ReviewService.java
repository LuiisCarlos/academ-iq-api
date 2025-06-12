package dev.luiiscarlos.academ_iq_api.review.service;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.course.model.Course;
import dev.luiiscarlos.academ_iq_api.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.review.dto.ReviewRequestDto;
import dev.luiiscarlos.academ_iq_api.review.dto.ReviewResponseDto;
import dev.luiiscarlos.academ_iq_api.review.exception.ReviewAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.review.exception.ReviewNotFoundException;
import dev.luiiscarlos.academ_iq_api.review.mapper.ReviewMapper;
import dev.luiiscarlos.academ_iq_api.review.model.Review;
import dev.luiiscarlos.academ_iq_api.review.repository.ReviewRepository;
import dev.luiiscarlos.academ_iq_api.user.model.User;
import dev.luiiscarlos.academ_iq_api.user.service.UserServiceImpl;
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
     * @param requestDto {@link ReviewRequestDto} The rating request data transfer
     *                   object containing the rating details
     * @return {@link ReviewResponseDto } The saved rating response data transfer
     *         object
     * @throws IllegalArgumentException if the user has already rated the course
     */
    public ReviewResponseDto save(String token, Long courseId, ReviewRequestDto requestDto) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        if (reviewRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new ReviewAlreadyExistsException("Failed to create rating: User already rated");

        Review review = reviewMapper.toModel(requestDto);
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
     * @param requestDto {@link ReviewRequestDto} The rating request data transfer
     *                   object containing the updated rating details
     * @return {@link ReviewResponseDto } The updated rating response data transfer
     *         object
     * @throws ReviewNotFoundException if the user has not rated the course
     */
    public ReviewResponseDto findByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        Review review = reviewRepository.findByUserIdAndCourseId(user.getId(), course.getId())
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Failed to retrieve rating: User not found"));

        return reviewMapper.toResponseDto(review);
    }

}
