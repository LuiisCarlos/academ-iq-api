package dev.luiiscarlos.academ_iq_api.features.file.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;

@Component
public class FileMapper {

    public FileResponse toFileResponseDto(File file) {
        return FileResponse.builder()
            .filename(file.getFilename())
            .contentType(file.getContentType())
            .size(file.getSize())
            .url(file.getUrl())
            .build();
    }

}
