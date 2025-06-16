package dev.luiiscarlos.academ_iq_api.features.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.features.category.dto.CategoryRequest;
import dev.luiiscarlos.academ_iq_api.features.category.mapper.CategoryMapper;
import dev.luiiscarlos.academ_iq_api.features.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.category.repository.CategoryRepository;
import dev.luiiscarlos.academ_iq_api.features.course.exception.CourseNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public Category create(CategoryRequest request) {
        Category category = categoryMapper.toModel(request);

        return categoryRepository.save(category);
    }

    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category get(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CourseNotFoundException("Category not found with ID " + categoryId));

        return category;
    }

    public Category get(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CourseNotFoundException("Category not found with name " + categoryName));

        return category;
    }

    public Category update(long categoryId, CategoryRequest request) {
        return categoryRepository.findById(categoryId).map(c -> {
            c.setName(request.getName());
            c.setSvg(request.getSvg());
            c.setShortDescription(request.getShortDescription());
            c.setLongDescription(request.getLongDescription());

            return categoryRepository.save(c);
        }).orElseThrow(() -> new CourseNotFoundException("Category not found with ID " + categoryId));
    }

    public void delete(long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CourseNotFoundException("Category not found with ID " + categoryId));

        categoryRepository.delete(category);
    }

}
