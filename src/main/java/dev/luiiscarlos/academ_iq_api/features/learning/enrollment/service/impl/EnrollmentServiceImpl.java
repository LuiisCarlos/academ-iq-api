package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.impl.CourseQueryService;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.exception.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.mapper.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.CompletedLesson;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.ProgressState;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.repository.EnrollmentRepository;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service.EnrollmentService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    private final CourseQueryService courseQueryService;

    @Override
    public EnrollmentResponse create(long userId, long courseId, @Nullable Map<String, Boolean> args) {
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) // TODO Change Exception
            throw new EnrollmentNotFoundException("Failed to save enrollment: User is already enrolled");

        User user = new User();
        user.setId(userId);

        Course course = new Course();
        course.setId(courseId);

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        if (Objects.nonNull(args) && args.containsKey("isFavorite"))
            enrollment.setFavorite(args.get("isFavorite"));

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public List<EnrollmentResponse> getAll(long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findAllByUserId(userId);

        if (enrollments.isEmpty() || enrollments == null)
            throw new EnrollmentNotFoundException("Failed to find enrollments: No enrollments found");

        return enrollments.stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    @Override
    public EnrollmentResponse get(long userId, long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException(
                        "Failed to find enrollment: Enrollment not found with course id: " + courseId));

        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public Enrollment getOrCreate(long userId, long courseId) {
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            return enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                    .orElseThrow(() -> new EnrollmentNotFoundException(
                            "Failed to find enrollment: Enrollment not found with course id: " + courseId));
        } else {
            User user = new User();
            user.setId(userId);

            Course course = new Course();
            course.setId(courseId);

            Enrollment enrollment = Enrollment.builder()
                    .user(user)
                    .course(course)
                    .build();

            return enrollmentRepository.save(enrollment);
        }
    }

    @Override
    public EnrollmentResponse update(long userId, long courseId, Map<String, Boolean> args) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
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

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse patchProgress(long userId, long courseId, Map<String, Object> args) {
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

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public void delete(long userId, long courseId) {
        if (!enrollmentRepository.existsByUserIdAndCourseId(userId, courseId))
            throw new EnrollmentNotFoundException(
                    "Failed to delete enrollment: Enrollment no found with course id " + courseId);

        enrollmentRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

    /**
     * Checks if the course is completed based on the enrollment's progress
     *
     * @param enrollment the enrollment to check
     */
    private void checkCourseCompletion(Enrollment enrollment) {
        List<Long> lessonIds = courseQueryService.findAllLessonIdsById(enrollment.getCourse().getId());

        if (lessonIds.isEmpty()) {
            enrollment.setCompleted(true);
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setProgress(1.0);
            return;
        }

        Set<Long> completedLessonIds = enrollment.getProgressState().getCompletedLessons().stream()
                .filter(cl -> Objects.nonNull(cl.getSectionId()) && Objects.nonNull(cl.getLessonId()))
                .map(CompletedLesson::getLessonId)
                .collect(Collectors.toSet());

        completedLessonIds.size();
        lessonIds.size();

        double progress = (double) completedLessonIds.size() / lessonIds.size();
        progress = Math.round(progress * 100.0) / 100.0;
        enrollment.setProgress(progress);
    }

}
