package dev.luiiscarlos.academ_iq_api.models.dtos.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseCategoryResponseDto {

    private Long id;

    private String name;

    private String shortDescription;

}
