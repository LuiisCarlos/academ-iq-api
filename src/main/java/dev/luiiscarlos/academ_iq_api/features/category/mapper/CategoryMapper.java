package dev.luiiscarlos.academ_iq_api.features.category.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.category.dto.CategoryRequest;
import dev.luiiscarlos.academ_iq_api.features.category.model.Category;

@Component
public class CategoryMapper {

    public Category toModel(CategoryRequest request) {
        return Category.builder()
            .name(request.getName())
            .svg(request.getSvg())
            .shortDescription(request.getShortDescription())
            .longDescription(request.getLongDescription())
            .build();
    }

}
