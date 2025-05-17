package dev.luiiscarlos.academ_iq_api.services;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.RatingAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.exceptions.RatingNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Rating;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.RatingRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.RatingResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.RatingMapper;
import dev.luiiscarlos.academ_iq_api.repositories.RatingRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    private final RatingMapper ratingMapper;

    private final UserServiceImpl userService;

    private final CourseService courseService;

    /**
     * Saves a rating for a course by a user
     *
     * @param token      The authentication token of the user
     * @param courseId   The ID of the course to be rated
     * @param requestDto {@link RatingRequestDto} The rating request data transfer
     *                   object containing the rating details
     *
     * @return {@link RatingResponseDto } The saved rating response data transfer
     *         object
     *
     * @throws IllegalArgumentException if the user has already rated the course
     */
    public RatingResponseDto save(String token, Long courseId, RatingRequestDto requestDto) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        if (ratingRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new RatingAlreadyExistsException("Failed to create rating: User already rated");

        Rating rating = ratingMapper.toModel(requestDto);
        rating.setUser(user);
        rating.setCourse(course);

        Rating response = ratingRepository.save(rating);

        return ratingMapper.toResponseDto(response);
    }

    /**
     * Updates a rating for a course by a user
     *
     * @param token      The authentication token of the user
     * @param courseId   The ID of the course to be rated
     * @param requestDto {@link RatingRequestDto} The rating request data transfer
     *                   object containing the updated rating details
     *
     * @return {@link RatingResponseDto } The updated rating response data transfer
     *         object
     *
     * @throws RatingNotFoundException if the user has not rated the course
     */
    public RatingResponseDto findByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        Rating response = ratingRepository.findByUserIdAndCourseId(user.getId(), course.getId())
                .orElseThrow(() -> new RatingNotFoundException(
                        "Failed to retrieve rating: User not found"));

        return ratingMapper.toResponseDto(response);
    }

}
