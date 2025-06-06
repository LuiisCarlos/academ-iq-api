package dev.luiiscarlos.academ_iq_api.services;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.review.ReviewAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.exceptions.review.ReviewNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Review;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.ReviewMapper;
import dev.luiiscarlos.academ_iq_api.repositories.ReviewRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository ratingRepository;

    private final ReviewMapper ratingMapper;

    private final UserServiceImpl userService;

    private final CourseService courseService;

    /**
     * Saves a rating for a course by a user
     *
     * @param token      The authentication token of the user
     * @param courseId   The ID of the course to be rated
     * @param requestDto {@link ReviewRequestDto} The rating request data transfer
     *                   object containing the rating details
     *
     * @return {@link ReviewResponseDto } The saved rating response data transfer
     *         object
     *
     * @throws IllegalArgumentException if the user has already rated the course
     */
    public ReviewResponseDto save(String token, Long courseId, ReviewRequestDto requestDto) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        if (ratingRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new ReviewAlreadyExistsException("Failed to create rating: User already rated");

        Review rating = ratingMapper.toModel(requestDto);
        rating.setUser(user);
        rating.setCourse(course);

        Review response = ratingRepository.save(rating);

        return ratingMapper.toResponseDto(response);
    }

    /**
     * Updates a rating for a course by a user
     *
     * @param token      The authentication token of the user
     * @param courseId   The ID of the course to be rated
     * @param requestDto {@link ReviewRequestDto} The rating request data transfer
     *                   object containing the updated rating details
     *
     * @return {@link ReviewResponseDto } The updated rating response data transfer
     *         object
     *
     * @throws ReviewNotFoundException if the user has not rated the course
     */
    public ReviewResponseDto findByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        Review response = ratingRepository.findByUserIdAndCourseId(user.getId(), course.getId())
                .orElseThrow(() -> new ReviewNotFoundException(
                        "Failed to retrieve rating: User not found"));

        return ratingMapper.toResponseDto(response);
    }

}
