package dev.luiiscarlos.academ_iq_api.features.review.dto;

import lombok.Data;

@Data
public class ReviewRequest {

    private Integer rating;

    private String comment;

}
