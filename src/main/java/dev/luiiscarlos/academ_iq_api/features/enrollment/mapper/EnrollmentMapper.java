package dev.luiiscarlos.academ_iq_api.features.enrollment.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.enrollment.model.Enrollment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentMapper {

    private final CourseMapper courseMapper;

    public EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
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
