package dev.luiiscarlos.academ_iq_api.category;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.course.exception.CourseNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Find all categories
     *
     * @return the list of the courses
     * @throws CourseNotFoundException if no categories are found
     */
    public List<Category> findAll() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty())
            throw new CourseNotFoundException(
                    "Failed to find category: No categories found");

        return categories;
    }

    /**
     *
     * @param categoryName
     * @return
     */
    public Category findByName(String categoryName) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CourseNotFoundException(
                        "Failed to find category: Category not found with name " + categoryName));

        return category;
    }

}
