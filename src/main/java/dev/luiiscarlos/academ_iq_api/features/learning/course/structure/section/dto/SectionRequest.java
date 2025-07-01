package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto.LessonRequest;
import lombok.Data;

@Data
public class SectionRequest {

    private String name;

    private Long duration;

    private List<LessonRequest> lessons;

}
