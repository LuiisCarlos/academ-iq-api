package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto;

import java.util.Set;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto.LessonResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private Long id;

    private String name;

    private Long duration;

    private Set<LessonResponse> lessons;

}
