package dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.lesson.mapper.LessonMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.Section;
import dev.luiiscarlos.academ_iq_api.features.learning.course.structure.section.dto.SectionResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SectionMapper {

    private final LessonMapper lessonMapper;

    public SectionResponse toDto(Section entity) {
        return SectionResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .duration(entity.getDuration().toMillis())
                .lessons(entity.getLessons().stream()
                        .map(lessonMapper::toDto)
                        .collect(Collectors.toSet()))
                .build();
    }

}
