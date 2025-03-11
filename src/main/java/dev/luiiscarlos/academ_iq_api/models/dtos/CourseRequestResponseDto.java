package dev.luiiscarlos.academ_iq_api.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseRequestResponseDto {

    private String name;

    private String description;

    private String author;

    private String thumbnailUrl;

    private String videoUrl;

    private String category;

    private String level;

    private String duration;
}
