package dev.luiiscarlos.academ_iq_api.features.course.structure.lesson.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicLessonResponse {

    private Long id;

    private String name;

}
