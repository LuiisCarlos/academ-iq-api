package dev.luiiscarlos.academ_iq_api.features.learning.review.dto;

import lombok.Data;

@Data
public class ReviewRequest {

    private Long courseId;

    private Integer rating;

    private String comment;

}
