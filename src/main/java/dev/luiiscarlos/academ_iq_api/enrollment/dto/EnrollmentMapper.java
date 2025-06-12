package dev.luiiscarlos.academ_iq_api.enrollment.dto;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.enrollment.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentMapper {

    private final CourseMapper courseMapper;

    public EnrollmentResponseDto toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponseDto.builder()
                .course(courseMapper.toEnrollmentCourseResponseDto(enrollment.getCourse()))
                .progress(enrollment.getProgress())
                .progressState(enrollment.getProgressState())
                .favorite(enrollment.isFavorite())
                .archived(enrollment.isArchived())
                .completed(enrollment.isCompleted())
                .enrolledAt(enrollment.getEnrolledAt())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }

}
