package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseProgressDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponseDto {

    private EnrollmentCourseResponseDto course;

    private double progress;

    private CourseProgressDto progressState;

    private Boolean isFavorite;

    private Boolean isArchived;

    private Boolean isCompleted;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime enrolledAt;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime completedAt;

}
