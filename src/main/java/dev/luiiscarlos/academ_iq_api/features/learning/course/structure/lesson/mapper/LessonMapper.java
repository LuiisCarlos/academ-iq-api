package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto.LessonResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.model.Lesson;

@Component
public class LessonMapper {

    public LessonResponse toDto(Lesson entity) {
        return LessonResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .video(entity.getVideo().getUrl())
                .build();
    }

}
