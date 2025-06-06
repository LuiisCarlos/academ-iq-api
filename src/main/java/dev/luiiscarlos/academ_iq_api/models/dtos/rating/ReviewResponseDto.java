package dev.luiiscarlos.academ_iq_api.models.dtos.rating;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {

    private Integer rating;

    private String comment;

    private String user;

    private String userAvatarUrl;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime ratedAt;

}
