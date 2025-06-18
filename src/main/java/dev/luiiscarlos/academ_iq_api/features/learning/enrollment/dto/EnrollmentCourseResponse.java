package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentCourseResponse {

    private Long id;

    private String title;

    private String instructor;

    private String thumbnailUrl;

    private String category;

    private String categorySvg;

    @JsonFormat(shape = Shape.STRING)
    private Duration duration;

}