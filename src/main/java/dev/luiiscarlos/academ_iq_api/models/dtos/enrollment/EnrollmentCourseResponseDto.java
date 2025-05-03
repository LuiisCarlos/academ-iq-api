package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import dev.luiiscarlos.academ_iq_api.models.Category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentCourseResponseDto {

    private long id;

    private String title;

    private String author;

    private String thumbnailUrl;

    private Category category;

}