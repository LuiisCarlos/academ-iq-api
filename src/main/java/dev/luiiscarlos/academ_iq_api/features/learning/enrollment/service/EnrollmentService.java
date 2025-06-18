package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.exception.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.mapper.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.CompletedLesson;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.ProgressState;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.repository.EnrollmentRepository;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl.UserQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService { // TODO Fix documentation

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    private final UserQueryService userQueryService;

    private final CourseService courseService;

    /**
     * Creates a new enrollment for a user in a specific course
     *
     * @param user     the current authenticated user
     * @param courseId the ID of the course
     * @param flags    a map containing optional enrollment flags
     * @return an {@link Enrollment} the newly created enrollment
     * @throws EnrollmentNotFoundException if the user is already enrolled in the
     *                                     course
     */
    public Enrollment create(long userId, long courseId, @Nullable Map<String, Boolean> args) {
        User user = userQueryService.findById(userId);
        Course course = courseService.findById(courseId);

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to save enrollment: User is already enrolled");

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        if (args != null)
            enrollment.setFavorite(args.getOrDefault("isFavorite", false));

        return enrollmentRepository.save(enrollment);
    }

    /**
     * Retrieves the list of enrollments for a specific user
     *
     * @param userId the ID of the authenticated user
     * @return an {@link EnrollmentResponse} containing enrollment data
     * @throws EnrollmentNotFoundException if no enrollments found for the given
     *                                     user
     */
    public List<EnrollmentResponse> getAll(long userId) {
        User user = userQueryService.findById(userId);
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
     * @return an {@link EnrollmentResponse} containing enrollment data
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public EnrollmentResponse get(long userId, long courseId) {
        User user = userQueryService.findById(userId);
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to find enrollment: Enrollment not found with course id: " + courseId));

        return enrollmentMapper.toEnrollmentResponse(enrollment);
    }

    /**
     *
     * @param token
     * @param courseId
     * @return
     */
    public Enrollment getOrCreate(long userId, long courseId) {
        User user = userQueryService.findById(userId);
        return enrollmentRepository
                .findByUserIdAndCourseId(user.getId(), courseId)
                .orElseGet(() -> this.create(userId, courseId, null));
    }

    /**
     * Partially updates enrollment properties (isFavorite, isArchived, isCompleted)
     * for a specific user and course.
     *
     * @param token    the authentication token of the user
     * @param courseId the ID of the course
     * @param updates  a map containing the fields to update and their new values
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public Enrollment update(long userId, long courseId, Map<String, Boolean> args) {
        User user = userQueryService.findById(userId);
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to update enrollment: Enrollment not found with course id " + courseId));

        if (args.containsKey("isFavorite"))
            enrollment.setFavorite(args.get("isFavorite"));

        if (args.containsKey("isArchived"))
            enrollment.setArchived(args.get("isArchived"));

        if (args.containsKey("isCompleted")) {
            enrollment.setCompleted(args.get("isCompleted"));
            enrollment.setProgress(1.0);
            enrollment.setCompletedAt(LocalDateTime.now());
        }

        return enrollmentRepository.save(enrollment);
    }

    /**
     *
     */
    public Enrollment patchProgress(long userId, long courseId, Map<String, Object> args) {
        Enrollment enrollment = this.getOrCreate(userId, courseId);
        ProgressState progressState = enrollment.getProgressState();

        long sectionId = (long) args.get("sectionId");
        long lessonId = (long) args.get("lessonId");
        Boolean isCompleted = (Boolean) args.get("isCompleted");

        progressState.setCurrentSectionId(sectionId);
        progressState.setCurrentLessonId(lessonId);

        if (isCompleted) {
            boolean alreadyCompleted = progressState.getCompletedLessons()
                    .stream()
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
     * @throws EnrollmentNotFoundException if no enrollment is found for the given
     *                                     user and course
     */
    public void delete(long userId, long courseId) {
        User user = userQueryService.findById(userId);

        if (!enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to delete enrollment: Enrollment no found with course id " + courseId);

        enrollmentRepository.deleteByUserIdAndCourseId(user.getId(), courseId);
    }

    /**
     *
     * @param enrollment
     */
    private void checkCourseCompletion(Enrollment enrollment) { // TODO Documentate this
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
