package dev.luiiscarlos.academ_iq_api.features.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {

    private String filename;

    private String contentType;

    private String url;

    private Long size;

}
