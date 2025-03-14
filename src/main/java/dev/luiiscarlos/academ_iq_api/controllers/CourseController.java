package dev.luiiscarlos.academ_iq_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.Course;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseRequestResponseDto;
import dev.luiiscarlos.academ_iq_api.services.CourseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> findById(@PathVariable Long id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findById(id));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseRequestResponseDto> save(@RequestBody CourseRequestResponseDto course,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("video") MultipartFile video) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.save(course, thumbnail, video));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseRequestResponseDto> updateById(@PathVariable Long id,
            @RequestBody CourseRequestResponseDto course,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart("video") MultipartFile video) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.updateById(id, course, thumbnail, video));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return null;
    }

}
