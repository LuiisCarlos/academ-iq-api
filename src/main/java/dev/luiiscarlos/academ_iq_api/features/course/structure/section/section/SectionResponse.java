package dev.luiiscarlos.academ_iq_api.features.course.structure.section.section;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.course.structure.lesson.dto.LessonResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private Long id;

    private String name;

    @JsonFormat(shape = Shape.STRING)
    private Duration duration;

    private List<LessonResponse> lessons;

}
