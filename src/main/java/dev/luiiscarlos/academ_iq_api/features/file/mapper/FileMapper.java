package dev.luiiscarlos.academ_iq_api.features.file.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.file.file.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;

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
