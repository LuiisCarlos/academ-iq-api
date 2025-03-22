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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.dtos.CourseRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.CourseResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.SectionResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.CourseMapper;
import dev.luiiscarlos.academ_iq_api.services.CourseService;
import dev.luiiscarlos.academ_iq_api.services.SectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    private final CourseMapper courseMapper;

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<CourseResponseDto> save(@RequestBody CourseRequestDto course) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.save(course));
    }

    @PostMapping(value = "/{id}/sections", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseDto> saveSectionByCourseId(@PathVariable Long id,
            @RequestParam String sectionName,
            @RequestPart MultipartFile ...videos) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(sectionService.save(id, sectionName, videos));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseMapper.toCourseResponseDto(courseService.findById(id)));
    }

    @GetMapping("/{id}/sections")
    public ResponseEntity<List<SectionResponseDto>> findAllSectionsByCourseId(@PathVariable Long id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(sectionService.findAll(id));
    }

    @GetMapping("/{id}/sections/{index}")
    public ResponseEntity<SectionResponseDto> findSectionByCourseIdAndSectionId(@PathVariable Long id,
            @PathVariable Integer index) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(sectionService.findById(id, index));
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<FileResponseDto> findThumbnailById(@PathVariable Long id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.findThumbnailById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateById(@PathVariable Long id, @RequestBody CourseRequestDto course) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.updateById(id, course));
    }

    @PutMapping("/{id}/sections/{index}")
    public ResponseEntity<SectionResponseDto> updateSectionByCourseIdAndSectionId(@PathVariable Long id,
            @PathVariable int index,
            @RequestParam String sectionName,
            @RequestPart MultipartFile ...videos) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(sectionService.updateById(id, index, sectionName, videos));
    }


    @PutMapping(value = "/{id}/thumbnail", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseDto> updateThumbnailById(@PathVariable Long id, @RequestPart MultipartFile thumbnail) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(courseService.updateThumbnailById(id, thumbnail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        courseService.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/{id}/sections/{index}")
    public ResponseEntity<Void> deleteSectionByCourseIdAndSectionId(@PathVariable Long id, @PathVariable int index) {
        sectionService.deleteById(id, index);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

}
