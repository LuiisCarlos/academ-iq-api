package dev.luiiscarlos.academ_iq_api.models.dtos.section;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.models.dtos.lesson.LessonProgressDto;

import lombok.Data;

@Data
public class SectionProgressDto {

    private Long sectionId;

    private boolean isCompleted;

    private List<LessonProgressDto> lessons;

}