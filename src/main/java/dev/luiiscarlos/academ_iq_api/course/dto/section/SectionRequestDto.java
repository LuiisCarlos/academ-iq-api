package dev.luiiscarlos.academ_iq_api.course.dto.section;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.course.dto.lesson.LessonRequestDto;

import lombok.Data;

@Data
public class SectionRequestDto {

    private String name;

    private String duration;

    private List<LessonRequestDto> lessons;

}
