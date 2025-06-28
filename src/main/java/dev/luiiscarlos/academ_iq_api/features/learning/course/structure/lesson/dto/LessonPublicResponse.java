package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonPublicResponse {

    private Long id;

    private String name;

}
