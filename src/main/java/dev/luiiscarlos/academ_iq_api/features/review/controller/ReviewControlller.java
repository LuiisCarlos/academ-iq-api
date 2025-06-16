package dev.luiiscarlos.academ_iq_api.features.review.controller;

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

import dev.luiiscarlos.academ_iq_api.features.review.dto.ReviewRequest;
import dev.luiiscarlos.academ_iq_api.features.review.dto.ReviewResponse;
import dev.luiiscarlos.academ_iq_api.features.review.service.ReviewService;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reviews")
public class ReviewControlller {

    private final ReviewService reviewService;

    @PostMapping("/{id}")
    public ResponseEntity<ReviewResponse> create(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long courseId,
            @RequestBody ReviewRequest requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.create(user, courseId, requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> retrieve(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long courseId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(reviewService.get(user, courseId));
    }

}