package dev.luiiscarlos.academ_iq_api.course.dto.section;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.course.dto.lesson.LessonResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponseDto {

    private Long id;

    private String name;

    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime duration;

    private List<LessonResponseDto> lessons;

}
