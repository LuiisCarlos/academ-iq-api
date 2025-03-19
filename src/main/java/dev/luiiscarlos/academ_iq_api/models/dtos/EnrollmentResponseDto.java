package dev.luiiscarlos.academ_iq_api.models.dtos;

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

    private String comment;

    private Integer rating;

    private Boolean isFavorite;

    private Boolean isArchived;

    private Boolean isCompleted;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime enrolledAt;

}
