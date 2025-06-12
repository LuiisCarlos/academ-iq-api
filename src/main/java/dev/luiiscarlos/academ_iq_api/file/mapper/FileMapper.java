package dev.luiiscarlos.academ_iq_api.file.mapper;

import dev.luiiscarlos.academ_iq_api.file.file.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.file.model.File;

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
