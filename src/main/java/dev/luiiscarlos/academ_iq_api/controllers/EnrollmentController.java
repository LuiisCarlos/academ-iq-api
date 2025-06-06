package dev.luiiscarlos.academ_iq_api.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.models.Enrollment;
import dev.luiiscarlos.academ_iq_api.models.dtos.enrollment.EnrollmentResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.services.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users/@me/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    private final EnrollmentMapper enrollmentMapper;

    @PostMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> create(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId,
            @RequestBody(required = false) Map<String, Boolean> flags) {
        Enrollment enrollment = enrollmentService.create(token, courseId, flags);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(enrollmentMapper.toEnrollmentResponse(enrollment));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDto>> findAllByUserId(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(enrollmentService.findAllByUserId(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> findByUserIdAndCourseId(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(enrollmentService.findByUserIdAndCourseId(token, courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDto> updateByUserIdAndCourseId(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId,
            @RequestBody Map<String, Boolean> updates) {
        Enrollment enrollment = enrollmentService.updateByUserIdAndCourseId(token, courseId, updates);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(enrollmentMapper.toEnrollmentResponse(enrollment));
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<EnrollmentResponseDto> patchProgressState(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId,
            @RequestBody Map<String, Object> updates) {
        Enrollment enrollment = enrollmentService.patchProgressState(token, courseId, updates);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(enrollmentMapper.toEnrollmentResponse(enrollment));
    }

    @DeleteMapping("/@me/{id}")
    public ResponseEntity<List<EnrollmentResponseDto>> deleteByUserIdAndCourseId(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId) {
        enrollmentService.deleteByUserIdAndCourseId(token, courseId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
