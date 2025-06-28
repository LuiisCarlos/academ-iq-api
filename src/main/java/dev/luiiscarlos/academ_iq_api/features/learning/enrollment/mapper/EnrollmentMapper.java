package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.learning.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentMapper {

    private final CourseMapper courseMapper;

    public EnrollmentResponse toDto(Enrollment entity) {
        return EnrollmentResponse.builder()
                .course(courseMapper.toEnrollmentDto(entity.getCourse()))
                .progress(entity.getProgress())
                .progressState(entity.getProgressState())
                .favorite(entity.isFavorite())
                .archived(entity.isArchived())
                .completed(entity.isCompleted())
                .enrolledAt(entity.getEnrolledAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }

}
