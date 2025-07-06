package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.features.learning.course.facade.CourseFacade;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.Course;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.exception.EnrollmentAlreadyExists;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.exception.EnrollmentNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.mapper.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.CompletedLesson;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.ProgressState;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.repository.EnrollmentRepository;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service.EnrollmentService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.facade.UserFacade;
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

    private final CourseFacade courseFacade;

    private final UserFacade userFacade;

    @Override
    public EnrollmentResponse create(long userId, long courseId, @Nullable Map<String, Boolean> args) {
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId))
            throw new EnrollmentAlreadyExists("User is already enrolled");

        User user = userFacade.getReferenceById(userId);
        Course course = courseFacade.getReferenceById(courseId);

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();

        if (Objects.nonNull(args) && args.containsKey("isFavorite"))
            enrollment.setFavorite(args.get("isFavorite"));

        return enrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    @Override
    public Page<EnrollmentResponse> getAll(Pageable pageable, long userId) {
        Page<Enrollment> enrollments = enrollmentRepository.findAllByUserId(pageable, userId);

        if (enrollments.isEmpty() || Objects.isNull(enrollments))
            throw new EnrollmentNotFoundException("No enrollments found");

        return enrollments.map(enrollmentMapper::toDto);
    }

    @Override
    public EnrollmentResponse get(long userId, long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with course id: " + courseId));

        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public Enrollment getOrCreate(long userId, long courseId) {
        if (enrollmentRepository.existsByUserIdAndCourseId(userId, courseId)) {
            return enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                    .orElseThrow(() -> new EnrollmentNotFoundException(
                            "Enrollment not found with course id: " + courseId));
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
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with course id " + courseId));

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
    public EnrollmentResponse patchState(long userId, long courseId, Map<String, Object> args) {
        Enrollment enrollment = this.getOrCreate(userId, courseId);
        ProgressState progressState = enrollment.getState();

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
            throw new EnrollmentNotFoundException("Enrollment no found with course id " + courseId);

        enrollmentRepository.deleteByUserIdAndCourseId(userId, courseId);
    }

    /**
     * Checks if the course is completed based on the current enrollment state
     *
     * @param enrollment the enrollment to check
     */
    private void checkCourseCompletion(Enrollment enrollment) {
        List<Long> lessonIds = courseFacade.findAllLessonIdsById(enrollment.getCourse().getId());

        if (lessonIds.isEmpty()) {
            enrollment.setCompleted(true);
            enrollment.setCompletedAt(LocalDateTime.now());
            enrollment.setProgress(1.0);
            return;
        }

        Set<Long> completedLessonIds = enrollment.getState().getCompletedLessons().stream()
                .filter(cl -> Objects.nonNull(cl.getSectionId()) && Objects.nonNull(cl.getLessonId()))
                .map(CompletedLesson::getLessonId)
                .collect(Collectors.toSet());

        double progress = (double) completedLessonIds.size() / lessonIds.size();
        progress = Math.round(progress * 100.0) / 100.0;
        enrollment.setProgress(progress);
    }

}
