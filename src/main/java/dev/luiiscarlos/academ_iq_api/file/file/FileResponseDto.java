package dev.luiiscarlos.academ_iq_api.file.file;

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
