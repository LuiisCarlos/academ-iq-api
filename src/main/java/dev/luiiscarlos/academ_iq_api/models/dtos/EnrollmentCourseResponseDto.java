package dev.luiiscarlos.academ_iq_api.models.dtos;

import dev.luiiscarlos.academ_iq_api.models.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentCourseResponseDto {

    private String name;

    private String author;

    private String thumbnailUrl;

    private Category category;

}