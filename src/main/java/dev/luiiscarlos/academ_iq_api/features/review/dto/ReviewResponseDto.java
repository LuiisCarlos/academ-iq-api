package dev.luiiscarlos.academ_iq_api.features.review.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponseDto {

    private String username;

    private String avatar;

    private Integer rating;

    private String comment;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime ratedAt;

}
