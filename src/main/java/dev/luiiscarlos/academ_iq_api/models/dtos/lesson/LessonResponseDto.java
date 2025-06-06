package dev.luiiscarlos.academ_iq_api.models.dtos.lesson;

import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponseDto {

    private Long id;

    private String name;

    private FileResponseDto file;

}
