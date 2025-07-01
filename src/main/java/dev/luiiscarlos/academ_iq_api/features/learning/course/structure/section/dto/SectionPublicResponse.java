package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto;


import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto.LessonPublicResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionPublicResponse {

    private Long id;

    private String name;

    @JsonFormat(shape = Shape.STRING)
    private Duration duration;

    private List<LessonPublicResponse> lessons;

}
