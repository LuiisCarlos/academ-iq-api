package dev.luiiscarlos.academ_iq_api.features.storage.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.storage.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;

@Component
public class FileMapper {

    public FileResponse toDto(File entity) {
        return FileResponse.builder()
            .filename(entity.getFilename())
            .contentType(entity.getContentType())
            .size(entity.getSize())
            .url(entity.getUrl())
            .build();
    }

}
