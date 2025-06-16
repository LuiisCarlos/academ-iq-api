package dev.luiiscarlos.academ_iq_api.features.course.structure.section.section;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.features.course.structure.lesson.dto.LessonRequest;
import lombok.Data;

@Data
public class SectionRequest {

    private String name;

    private Long duration;

    private List<LessonRequest> lessons;

}
