package dev.luiiscarlos.academ_iq_api.features.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String shortDescription;

}
