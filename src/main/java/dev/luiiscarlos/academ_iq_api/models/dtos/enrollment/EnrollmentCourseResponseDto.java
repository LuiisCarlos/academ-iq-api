package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentCourseResponseDto {

    private Long id;

    private String title;

    private String instructor;

    private String thumbnailUrl;

    private String category;

    private String categorySvg;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

}