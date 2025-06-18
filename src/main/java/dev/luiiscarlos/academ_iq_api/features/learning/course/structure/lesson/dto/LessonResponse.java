package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonResponse {

    private Long id;

    private String name;

    private FileResponse video;

}
