package dev.luiiscarlos.academ_iq_api.features.enrollment.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.enrollment.model.ProgressState;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponse {

    private EnrollmentCourseResponse course;

    private Double progress;

    private ProgressState progressState;

    private Boolean favorite;

    private Boolean archived;

    private Boolean completed;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime enrolledAt;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime completedAt;

}
