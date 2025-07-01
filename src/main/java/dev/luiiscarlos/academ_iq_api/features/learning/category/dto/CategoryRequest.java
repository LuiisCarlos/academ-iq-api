package dev.luiiscarlos.academ_iq_api.features.learning.category.dto;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private String svg;

    private String shortDescription;

    private String longDescription;


}
