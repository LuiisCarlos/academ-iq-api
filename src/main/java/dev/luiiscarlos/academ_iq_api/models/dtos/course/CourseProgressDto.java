package dev.luiiscarlos.academ_iq_api.models.dtos.course;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.models.dtos.section.SectionProgressDto;

import lombok.Data;

@Data
public class CourseProgressDto {

    private Long currentSectionId;

    private Long currentLessonId;

    private List<SectionProgressDto> sections;

}