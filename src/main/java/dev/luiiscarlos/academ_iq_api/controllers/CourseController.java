package dev.luiiscarlos.academ_iq_api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.Category;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.course.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.CourseMapper;
import dev.luiiscarlos.academ_iq_api.services.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    private final CourseMapper courseMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseDto> save(
            @RequestPart("course") CourseRequestDto courseDto,
            @RequestParam Map<String, MultipartFile> files) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.save(courseDto, files));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> findById(@PathVariable("id") Long courseId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseMapper.toCourseResponseDto(courseService.findById(courseId)));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> findAllCategories() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findAllCategories());
    }

    @GetMapping("categories/{category}")
    public ResponseEntity<Category> findCategoryByName(@PathVariable("category") String categoryName ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findCategoryByName(categoryName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateById(
            @PathVariable("id") Long courseId,
            @RequestPart("course") CourseRequestDto courseDto,
            @RequestParam Map<String, MultipartFile> files) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.updateById(courseId, courseDto, files));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long courseId) {
        courseService.deleteById(courseId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

}
