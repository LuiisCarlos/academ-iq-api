package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.dto.EnrollmentResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.mapper.EnrollmentMapper;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/enrollments")
public class EnrollmentController {

	private final EnrollmentService enrollmentService;

	private final EnrollmentMapper enrollmentMapper;

	@PostMapping("/@me/{id}")
	public ResponseEntity<EnrollmentResponse> create(
			@AuthenticationPrincipal Long userId,
			@PathVariable("id") Long courseId,
			@RequestBody(required = false) Map<String, Boolean> args) {
		Enrollment enrollment = enrollmentService.create(userId, courseId, args);

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(enrollmentMapper.toEnrollmentResponse(enrollment));
	}

	@GetMapping
	public ResponseEntity<List<EnrollmentResponse>> getAll(@AuthenticationPrincipal Long userId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(enrollmentService.getAll(userId));
	}

	@GetMapping("/@me/{id}")
	public ResponseEntity<EnrollmentResponse> get(
			@AuthenticationPrincipal Long userId,
			@PathVariable("id") Long courseId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(enrollmentService.get(userId, courseId));
	}

	@PutMapping("/@me/{id}")
	public ResponseEntity<EnrollmentResponse> update(
			@AuthenticationPrincipal Long userId,
			@PathVariable("id") Long courseId,
			@RequestBody Map<String, Boolean> args) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(enrollmentMapper.toEnrollmentResponse(enrollmentService.update(userId, courseId, args)));
	}

	@PatchMapping("/@me/{id}/progress")
	public ResponseEntity<EnrollmentResponse> patchProgressState(
			@AuthenticationPrincipal Long userId,
			@PathVariable("id") Long courseId,
			@RequestBody Map<String, Object> args) {
		Enrollment enrollment = enrollmentService.patchProgress(userId, courseId, args);

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(enrollmentMapper.toEnrollmentResponse(enrollment));
	}

	@DeleteMapping("/@me/{id}")
	public ResponseEntity<List<EnrollmentResponse>> delete(
			@AuthenticationPrincipal Long userId,
			@PathVariable("id") Long courseId) {
		enrollmentService.delete(userId, courseId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
