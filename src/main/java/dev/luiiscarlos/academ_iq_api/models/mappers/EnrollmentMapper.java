package dev.luiiscarlos.academ_iq_api.models.mappers;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.Enrollment;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentResponseDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EnrollmentMapper {

    private final CourseMapper courseMapper;

    public EnrollmentResponseDto toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponseDto.builder()
            .course(courseMapper.toEnrollmentCourseResponseDto(enrollment.getCourse()))
            .progress(enrollment.getProgress())
            .rating(enrollment.getRating())
            .comment(enrollment.getComment())
            .isFavorite(enrollment.isFavorite())
            .isArchived(enrollment.isArchived())
            .isCompleted(enrollment.isCompleted())
            .enrolledAt(enrollment.getEnrolledAt())
            .build();
    }

    public Enrollment toEnrollment(User user, Course course) {
        return Enrollment.builder()
            .user(user)
            .course(course)
            .build();
    }

}
