package dev.luiiscarlos.academ_iq_api.file.mappers;

import dev.luiiscarlos.academ_iq_api.file.File;
import dev.luiiscarlos.academ_iq_api.file.file.FileResponseDto;

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
