package dev.luiiscarlos.academ_iq_api.features.learning.category.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.features.learning.category.dto.CategoryRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.category.model.Category;
import dev.luiiscarlos.academ_iq_api.features.learning.category.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.create(request));
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> update(@PathVariable("id") Long categoryId, @RequestBody CategoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryService.update(categoryId, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long categoryId) {
        categoryService.delete(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}