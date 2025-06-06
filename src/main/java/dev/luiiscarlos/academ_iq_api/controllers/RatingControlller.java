package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.rating.ReviewResponseDto;
import dev.luiiscarlos.academ_iq_api.services.RatingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingControlller {

    private final RatingService ratingService;

    @PostMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> create(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId,
            @RequestBody ReviewRequestDto requestDto) {
        ReviewResponseDto responseDto = ratingService.save(token, courseId, requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> retrieve(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long courseId) {
        ReviewResponseDto responseDto = ratingService.findByUserIdAndCourseId(token, courseId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
    }

}