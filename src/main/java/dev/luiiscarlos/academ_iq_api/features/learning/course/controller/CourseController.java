package dev.luiiscarlos.academ_iq_api.features.learning.course.controller;

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

import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.CourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.dto.PublicCourseResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.course.mapper.CourseMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.course.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/courses")
public class CourseController {

    private final CourseService courseService;

    private final CourseMapper courseMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponse> create(
            @RequestPart("course") CourseRequest courseDto,
            @RequestParam Map<String, MultipartFile> files) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.create(courseDto, files));
    }

    @GetMapping
    public ResponseEntity<List<PublicCourseResponse>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> findById(@PathVariable("id") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseMapper.toResponseDto(courseService.findById(courseId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateById(
            @PathVariable("id") Long courseId,
            @RequestPart("course") CourseRequest courseDto,
            @RequestParam Map<String, MultipartFile> files) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.updateById(courseId, courseDto, files));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long courseId) {
        courseService.deleteById(courseId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
