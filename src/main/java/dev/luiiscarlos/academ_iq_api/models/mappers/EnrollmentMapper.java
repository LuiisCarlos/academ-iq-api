package dev.luiiscarlos.academ_iq_api.models.mappers;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Enrollment;
import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseProgressDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.enrollment.EnrollmentResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.lesson.LessonProgressDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionProgressDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentMapper {

    private final CourseMapper courseMapper;

    private final ObjectMapper objectMapper;

    public EnrollmentResponseDto toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponseDto.builder()
                .course(courseMapper.toEnrollmentCourseResponseDto(enrollment.getCourse()))
                .progress(this.getProgress(enrollment.getProgressState(), enrollment.getCourse()))
                .progressState(this.parseProgressJson(enrollment.getProgressState()))
                .isFavorite(enrollment.isFavorite())
                .isArchived(enrollment.isArchived())
                .isCompleted(enrollment.isCompleted())
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }

    public Enrollment toEnrollment(User user, Course course) {
        return Enrollment.builder()
                .user(user)
                .course(course)
                .build();
    }

    private CourseProgressDto parseProgressJson(String progress) {
        if (progress == null || progress.isBlank())
            return null;

        try {
            return objectMapper.readValue(progress, CourseProgressDto.class);
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse progress JSON: " + progress, e);
            return null;
        }
    }

    private Double getProgress(String progressState, Course course) {
        try {
            CourseProgressDto state = this.objectMapper.readValue(progressState, CourseProgressDto.class);
            return calculateProgress(state, calculateTotalLessons(course));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private double calculateProgress(CourseProgressDto state, int totalLessons) {
        int completedLessons = 0;

        if (state.getSections() != null) {
            for (SectionProgressDto section : state.getSections()) {
                if (section.getLessons() != null) {
                    completedLessons += section.getLessons().stream()
                            .filter(LessonProgressDto::isCompleted).count();
                }
            }
        }

        return totalLessons == 0 ? 0.0 : (double) completedLessons / totalLessons;
    }

    @SuppressWarnings("null")
    private int calculateTotalLessons(Course course) {
        int totalLessons = 0;

        if (course.getSections() != null) {
            for (Section section : course.getSections()) {
                if (section.getLessons() != null) {
                    totalLessons += section.getLessons().size();
                }
            }
        }

        return totalLessons;
    }

}
