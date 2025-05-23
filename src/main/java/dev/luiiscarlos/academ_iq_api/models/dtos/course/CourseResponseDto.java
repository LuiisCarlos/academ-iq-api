package dev.luiiscarlos.academ_iq_api.models.dtos.course;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.models.Category;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.RatingResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponseDto {

    private long id;

    private String title;

    private String subtitle;

    private String description;

    private String author;

    private String thumbnailUrl;

    private Category category;

    private String level;

    private double averageRating;

    private List<RatingResponseDto> ratings;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private List<String> requirements;

    private List<SectionResponseDto> sections;

}
