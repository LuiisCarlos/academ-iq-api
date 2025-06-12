package dev.luiiscarlos.academ_iq_api.features.course.dto.course;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseLessonResponseDto {

    private Long id;

    private String name;

}
