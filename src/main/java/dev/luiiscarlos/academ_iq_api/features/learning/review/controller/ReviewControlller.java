package dev.luiiscarlos.academ_iq_api.features.learning.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.learning.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.learning.review.service.ReviewService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reviews")
public class ReviewControlller { // Create proper Course review endpoint

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody ReviewRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.create(userId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Page<ReviewResponse>> getAll(Pageable pageable, @PathVariable("id") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.getAll(pageable, courseId));
    }

   /*  @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> get(
            @AuthenticationPrincipal Long userId,
            @PathVariable("id") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.get(userId, courseId));
    } */

}