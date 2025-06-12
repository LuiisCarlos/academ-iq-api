package dev.luiiscarlos.academ_iq_api.features.course.dto.section;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.features.course.dto.lesson.LessonRequest;

import lombok.Data;

@Data
public class SectionRequest {

    private String name;

    private Long duration;

    private List<LessonRequest> lessons;

}
