package dev.luiiscarlos.academ_iq_api.course.dto.course;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicCourseResponseDto {

    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private String instructor;

    private String thumbnail;

    private String level;

    private Double rating;

    private Integer reviews;

    private Integer sections;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    private CourseCategoryResponseDto category;

    private List<String> requirements;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

}
