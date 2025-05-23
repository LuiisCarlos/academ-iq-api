package dev.luiiscarlos.academ_iq_api.models.dtos.section;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.models.dtos.lesson.LessonRequestDto;
import lombok.Data;

@Data
public class SectionRequestDto {

    private String name;

    private String duration;

    private List<LessonRequestDto> lessons;

}
