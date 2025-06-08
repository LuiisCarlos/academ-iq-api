package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponseDto {

    private EnrollmentCourseResponseDto course;

    private Double progress;

    private ProgressState progressState;

    private Boolean favorite;

    private Boolean archived;

    private Boolean completed;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime enrolledAt;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime completedAt;

}
