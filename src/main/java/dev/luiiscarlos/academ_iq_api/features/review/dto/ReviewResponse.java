package dev.luiiscarlos.academ_iq_api.features.review.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {

    private String username;

    private String avatar;

    private String comment;

    private Integer rating;

    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime ratedAt;

}
