package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.course.CourseNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.Category;
import dev.luiiscarlos.academ_iq_api.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Find all categories
     *
     * @return the list of the courses
     *
     * @throws CourseNotFoundException if no categories are found
     */
    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty())
            throw new CourseNotFoundException(
                    "Failed to find category: No categories found");

        return categories;
    }

    public Category findCategoryByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Failed to find category: Category not found with name " + categoryName));

        return category;
    }

}
