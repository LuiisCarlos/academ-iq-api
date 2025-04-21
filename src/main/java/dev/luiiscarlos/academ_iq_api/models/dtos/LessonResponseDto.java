package dev.luiiscarlos.academ_iq_api.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponseDto {

    private String name;

    private String lessonUrl;

}
