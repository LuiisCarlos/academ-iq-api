package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentResponseDto;
import dev.luiiscarlos.academ_iq_api.exceptions.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidRatingException;
import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Enrollment;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.mappers.EnrollmentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final UserServiceImpl userService;

    private final EnrollmentMapper enrollmentMapper;

    private final CourseService courseService;

    /**
     * Save a new enrollment and sets the relationship between user and course
     *
     * @param token the user's token
     * @param courseId the course's id
     * @return the user's enrollments
     */
    @SuppressWarnings("null") // Already handled
    public List<EnrollmentResponseDto> saveEnrollmentByToken(String token, Long courseId) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);
        Enrollment enrollment = enrollmentMapper.toEnrollment(user, course);
        List<Enrollment> enrollments = user.getEnrollments();

        if (enrollments.stream().anyMatch(e -> e.getCourse().getId().equals(courseId)))
            throw new EnrollmentNotFoundException(
                "Failed to save enrollment: Enrollment already exists by course id " + courseId);

        enrollments.add(enrollment);
        user.setEnrollments(enrollments);
        user = userService.save(user);

        return user.getEnrollments().stream()
            .map(enrollmentMapper::toEnrollmentResponse)
            .toList();
    }

    /**
     * Finds the user's enrollments
     *
     * @param token the user's token
     * @return the user's enrollments
     */
    public List<EnrollmentResponseDto> findEnrollmentsByToken(String token) {
        User user = userService.findByToken(token);

        if (user.getEnrollments().isEmpty())
            throw new EnrollmentNotFoundException("Failed to find enrollments: No enrollments found");

        return user.getEnrollments().stream()
            .map(enrollmentMapper::toEnrollmentResponse)
            .toList();
    }

    /**
     * Updates the user's enrollment by its course id
     *
     * @param token the user's token
     * @param enrollmentRequest the enrollment details
     * @return the user's enrollments
     */
    @SuppressWarnings("null") // Already handled
    public List<EnrollmentResponseDto> updateEnrollmentByToken(String token,
            EnrollmentRequestDto enrollmentRequest) {
        User user = userService.findByToken(token);
        List<Enrollment> enrollments = user.getEnrollments();

        Enrollment enrollment = enrollments.stream()
            .filter(e -> e.getCourse().getId().equals(enrollmentRequest.getCourseId()))
            .findFirst()
            .orElseThrow(() -> new EnrollmentNotFoundException(
                "Failed to update enrollment: Enrollment not found with course id " + enrollmentRequest.getCourseId()));

        if (enrollmentRequest.getRating() < 1 || enrollmentRequest.getRating() > 5)
            throw new InvalidRatingException("Failed to update enrollment: Rating must be between 1 and 5");

        enrollment.setRating(enrollmentRequest.getRating());
        enrollment.setComment(enrollmentRequest.getComment());
        enrollment.setProgress(enrollmentRequest.getProgress());
        enrollment.setFavorite(enrollmentRequest.isFavorite());
        enrollment.setArchived(enrollmentRequest.isArchived());
        enrollment.setCompleted(enrollmentRequest.isCompleted());

        List<Enrollment> courseEnrollments = enrollment.getCourse().getEnrollments();
        List<Integer> ratings = courseEnrollments.stream()
            .map(Enrollment::getRating)
            .filter(r -> r > 0)
            .toList();

        Double averageRating = ratings.isEmpty() ?
            0 : ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
        enrollment.getCourse().setRating(averageRating);

        user = userService.save(user);

        return user.getEnrollments().stream()
            .map(enrollmentMapper::toEnrollmentResponse)
            .toList();
    }

    /**
     * Deletes the user's enrollment by its course id
     *
     * @param token the user's token
     * @param courseId the enrollment course id
     */
    @SuppressWarnings("null") // Already handled
    public List<EnrollmentResponseDto> deleteEnrollmentByToken(String token, Long courseId) {
        User user = userService.findByToken(token);
        List<Enrollment> enrollments = user.getEnrollments();

        if (enrollments.stream().noneMatch(e -> e.getCourse().getId().equals(courseId)))
            throw new EnrollmentNotFoundException(
                "Failed to update enrollment: Enrollment not found with course id" + courseId);

        enrollments.removeIf(e -> e.getCourse().getId().equals(courseId));
        user.setEnrollments(enrollments);
        userService.save(user);

        return user.getEnrollments().stream()
            .map(enrollmentMapper::toEnrollmentResponse)
            .toList();
    }
}