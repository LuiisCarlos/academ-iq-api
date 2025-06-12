package dev.luiiscarlos.academ_iq_api.course.dto.course;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.course.dto.section.SectionResponseDto;
import dev.luiiscarlos.academ_iq_api.review.dto.ReviewResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponseDto {

    private Long id;

    private String title;

    private String subtitle;

    private String description;

    private String instructor;

    private String thumbnail;

    private CourseCategoryResponseDto category;

    private String level;

    private Double rating;

    private List<ReviewResponseDto> reviews;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private List<String> requirements;

    private List<SectionResponseDto> sections;

}
