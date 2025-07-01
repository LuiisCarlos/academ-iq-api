package dev.luiiscarlos.academ_iq_api.features.learning.category.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.learning.category.dto.CategoryRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.category.dto.CategoryResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.category.model.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest dto) {
        return Category.builder()
                .name(dto.getName())
                .svg(dto.getSvg())
                .shortDescription(dto.getShortDescription())
                .longDescription(dto.getLongDescription())
                .build();
    }

    public CategoryResponse toDto(Category entity) {
        return CategoryResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getShortDescription())
                .build();
    }

}
