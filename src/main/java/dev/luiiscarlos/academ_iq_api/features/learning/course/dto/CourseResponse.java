package dev.luiiscarlos.academ_iq_api.features.learning.course.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserInstructorResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.category.dto.CategoryResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseAccess;
import dev.luiiscarlos.academ_iq_api.features.learning.course.model.CourseLevel;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto.SectionResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponse {

    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private UserInstructorResponse instructor;

    private String thumbnail;

    @JsonFormat(shape = Shape.STRING)
    private CourseAccess access;

    @JsonFormat(shape = Shape.STRING)
    private CourseLevel level;

    private Double price;

    private Double rating;

    private Long duration;

    private CategoryResponse category;

    private Set<String> requirements;

    private Integer reviews;

    private Set<SectionResponse> sections;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime createdAt;

}
