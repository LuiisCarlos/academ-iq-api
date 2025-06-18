package dev.luiiscarlos.academ_iq_api.features.learning.course.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.learning.category.dto.CategoryResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseAccess;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseLevel;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.section.SectionResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponse {

    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private InstructorResponse instructor;

    private String thumbnail;

    @JsonFormat(shape = Shape.STRING)
    private CourseAccess access;

    @JsonFormat(shape = Shape.STRING)
    private CourseLevel level;

    private Double price;

    private Double rating;

    @JsonFormat(shape = Shape.STRING)
    private Duration duration;

    private CategoryResponse category;

    private List<String> requirements;

    private List<ReviewResponse> reviews;

    private List<SectionResponse> sections;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime createdAt;

}
