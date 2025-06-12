package dev.luiiscarlos.academ_iq_api.features.course.dto.course;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.category.dto.CategoryResponse;
import dev.luiiscarlos.academ_iq_api.features.course.model.Course.Level;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicCourseResponse {

    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private String instructor;

    private String thumbnail;

    @JsonFormat(shape = Shape.STRING)
    private Level level;

    private Double price;

    private Double rating;

    @JsonFormat(shape = Shape.STRING)
    private Duration duration;

    private CategoryResponse category;

    private List<String> requirements;

    private Integer reviews;

    private Integer sections;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime createdAt;

}
