package dev.luiiscarlos.academ_iq_api.models.dtos;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseResponseDto {

    private String name;

    private String description;

    private String author;

    private String thumbnailUrl;

    private String category;

    private String level;

    private Double rating;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    private List<String> recommendedRequirements;

    private List<SectionResponseDto> sections;

}
