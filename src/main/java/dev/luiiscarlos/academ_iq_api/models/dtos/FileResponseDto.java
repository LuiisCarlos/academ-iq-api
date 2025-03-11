package dev.luiiscarlos.academ_iq_api.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDto {

    private String filename;

    private String contentType;

    private Long size;

    private String url;
}
