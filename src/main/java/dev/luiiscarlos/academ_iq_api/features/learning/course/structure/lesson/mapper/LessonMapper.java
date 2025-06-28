package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.Lesson;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.dto.LessonResponse;

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
