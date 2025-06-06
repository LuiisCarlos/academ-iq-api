package dev.luiiscarlos.academ_iq_api.models.dtos.rating;

import lombok.Data;

@Data
public class ReviewRequestDto {

    private Integer rating;

    private String comment;

}
