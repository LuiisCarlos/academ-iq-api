package dev.luiiscarlos.academ_iq_api.models.mappers;

import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;

import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileResponseDto toFileResponseDto(File file) {
        return FileResponseDto.builder()
            .filename(file.getFilename())
            .contentType(file.getContentType())
            .size(file.getSize())
            .url(file.getUrl())
            .build();
    }

}
