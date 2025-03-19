package dev.luiiscarlos.academ_iq_api.models.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.SectionResponseDto;

@Component
public class SectionMapper {

    @SuppressWarnings("null") // TODO: Review this
    public SectionResponseDto toSectionResponseDto(Section section) {
        List<String> videosUrl = section.getVideos().stream()
            .map(s -> s.getUrl()).toList();

        return SectionResponseDto.builder()
            .name(section.getName())
            .videosUrl(videosUrl)
            .build();
    }

}
