package dev.luiiscarlos.academ_iq_api.course.dto.lesson;

import dev.luiiscarlos.academ_iq_api.file.file.FileResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponseDto {

    private Long id;

    private String name;

    private FileResponseDto video;

}
