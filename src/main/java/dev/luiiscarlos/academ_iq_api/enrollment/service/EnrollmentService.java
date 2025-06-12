package dev.luiiscarlos.academ_iq_api.enrollment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.course.exception.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.course.model.Course;
import dev.luiiscarlos.academ_iq_api.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.enrollment.dto.CompletedLesson;
import dev.luiiscarlos.academ_iq_api.enrollment.dto.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.enrollment.dto.EnrollmentResponseDto;
import dev.luiiscarlos.academ_iq_api.enrollment.dto.ProgressState;
import dev.luiiscarlos.academ_iq_api.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.enrollment.repository.EnrollmentRepository;
import dev.luiiscarlos.academ_iq_api.user.model.User;
import dev.luiiscarlos.academ_iq_api.user.service.UserServiceImpl;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    private final UserServiceImpl userService;

    private final CourseService courseService;

    /**
     * Creates a new enrollment for a user in a specific course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     * @param flags    a map containing optional enrollment flags
     *
     * @return an {@link Enrollment} the newly created enrollment
     *
     * @throws EnrollmentNotFoundException if the user is already enrolled in the
     *                                     course
     */
    public Enrollment create(String token, Long courseId, @Nullable Map<String, Boolean> flags) {
        User user = userService.findByToken(token);
        Course course = courseService.findById(courseId);

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to save enrollment: User is already enrolled");

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        if (flags != null)
            enrollment.setFavorite(flags.getOrDefault("isFavorite", false));

        return enrollmentRepository.save(enrollment);
    }

    /**
     *
     * @param token
     * @param courseId
     * @return
     */
    public Enrollment findOrCreate(String token, Long courseId) {
        User user = userService.findByToken(token);
        return enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseGet(() -> create(token, courseId, null));
    }

    /**
     * Retrieves the list of enrollments for a specific user
     *
     * @param token the authentication token of the user
     *
     * @return an {@link EnrollmentResponseDto} containing enrollment data
     *
     * @throws EnrollmentNotFoundException if no enrollments found for the given
     *                                     user
     */
    public List<EnrollmentResponseDto> findAllByUserId(String token) {
        User user = userService.findByToken(token);
        List<Enrollment> enrollments = enrollmentRepository.findAllByUserId(user.getId());

        if (enrollments.isEmpty() || enrollments == null)
            throw new EnrollmentNotFoundException("Failed to find enrollments: No enrollments found");

        return enrollments.stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();
    }

    /**
     * Retrieves the enrollment information for a specific user and course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     *
     * @return an {@link EnrollmentResponseDto} containing enrollment data
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public EnrollmentResponseDto findByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to find enrollment: Enrollment not found with course id: " + courseId));

        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    /**
     * Partially updates enrollment properties (isFavorite, isArchived, isCompleted)
     * for a specific user and course.
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     * @param updates  a map containing the fields to update and their new values
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public Enrollment updateByUserIdAndCourseId(String token, Long courseId, Map<String, Boolean> updates) {
        User user = userService.findByToken(token);
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to update enrollment: Enrollment not found with course id " + courseId));

        if (updates.containsKey("isFavorite"))
            enrollment.setFavorite(updates.get("isFavorite"));

        if (updates.containsKey("isArchived"))
            enrollment.setArchived(updates.get("isArchived"));

        if (updates.containsKey("isCompleted")) {
            enrollment.setCompleted(updates.get("isCompleted"));
            enrollment.setProgress(1.0);
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        return enrollmentRepository.save(enrollment);
    }

    /**
     *
     */
    public Enrollment patchProgressState(String token, Long courseId, Map<String, Object> updates) {
        Enrollment enrollment = findOrCreate(token, courseId);
        ProgressState progressState = enrollment.getProgressState();

        Long sectionId = Long.valueOf((int) updates.get("sectionId"));
        Long lessonId = Long.valueOf((int) updates.get("lessonId"));
        Boolean isCompleted = (Boolean) updates.get("isCompleted");

        progressState.setCurrentSectionId(sectionId);
        progressState.setCurrentLessonId(lessonId);

        if (isCompleted) {
            boolean alreadyCompleted = progressState.getCompletedLessons().stream()
                    .anyMatch(cl -> cl.getSectionId().equals(sectionId) && cl.getLessonId().equals(lessonId));

            if (!alreadyCompleted) {
                CompletedLesson completedLesson = new CompletedLesson();
                completedLesson.setSectionId(sectionId);
                completedLesson.setLessonId(lessonId);
                completedLesson.setCompletedAt(LocalDateTime.now());
                progressState.getCompletedLessons().add(completedLesson);

            }
        }

        checkCourseCompletion(enrollment);

        return enrollmentRepository.save(enrollment);
    }

    /**
     * Deletes the enrollment for a specific user and course
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     *
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public void deleteByUserIdAndCourseId(String token, Long courseId) {
        User user = userService.findByToken(token);

        if (!enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to delete enrollment: Enrollment no found with course id " + courseId);

        enrollmentRepository.deleteByUserIdAndCourseId(user.getId(), courseId);
    }

    /**
     *
     * @param enrollment
     */
    private void checkCourseCompletion(Enrollment enrollment) {
        List<Long> allLessonIds = courseService.findAllLessonIdsById(enrollment.getCourse().getId());

        if (allLessonIds.isEmpty()) {
            enrollment.setCompleted(true);
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setProgress(1.0);
            return;
        }

        Set<Long> completedLessonIds = enrollment.getProgressState().getCompletedLessons().stream()
                .filter(cl -> cl.getSectionId() != null && cl.getLessonId() != null)
                .map(CompletedLesson::getLessonId)
                .collect(Collectors.toSet());

        completedLessonIds.size();
        allLessonIds.size();

        double progress = (double) completedLessonIds.size() / allLessonIds.size();
        progress = Math.round(progress * 100.0) / 100.0;
        enrollment.setProgress(progress);
    }

}
