package dev.luiiscarlos.academ_iq_api.models.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Section;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.SectionResponseDto;

@Component
public class SectionMapper {

    @SuppressWarnings("null") // TODO: Review this
    public SectionResponseDto toSectionResponseDto(Section section) {
        List<FileResponseDto> videos = section.getVideos().stream()
            .map(video -> FileResponseDto.builder()
                .filename(video.getFilename())
                .contentType(video.getContentType())
                .size(video.getSize())
                .url(video.getUrl())
                .build())
            .toList();

        return SectionResponseDto.builder()
            .name(section.getName())
            .videos(videos)
            .build();
    }

}
