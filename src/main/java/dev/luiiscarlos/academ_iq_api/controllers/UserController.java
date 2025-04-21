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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.EnrollmentResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.PasswordUpdateDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.EnrollmentService;
import dev.luiiscarlos.academ_iq_api.services.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    private final EnrollmentService enrollmentService;

    private final UserMapper userMapper;

    @PostMapping("/@me/change-password")
    public ResponseEntity<Void> updatePasswordByToken(
            @RequestHeader("Authorization") String token,
            @RequestBody PasswordUpdateDto passwordDto) {
        userService.updatePasswordByToken(token, passwordDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/@me/enrollments")
    public ResponseEntity<List<EnrollmentResponseDto>> saveEnrollmentByToken(
            @RequestHeader("Authorization") String token,
            @RequestParam("id") Long courseId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(enrollmentService.saveEnrollmentByToken(token, courseId));
    }

    @GetMapping("/@me")
    public ResponseEntity<UserResponseDto> findByToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userMapper.toUserResponseDto(userService.findByToken(token)));
    }

    @GetMapping("/@me/enrollments")
    public ResponseEntity<List<EnrollmentResponseDto>> findEnrollmentsByToken(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(enrollmentService.findEnrollmentsByToken(token));
    }

    @PutMapping("/@me")
    public ResponseEntity<UserResponseDto> updateByToken(@RequestHeader("Authorization") String token) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userMapper.toUserResponseDto(userService.updateByToken(token)));
    }

    @PutMapping(value = "/@me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> updateAvatarByToken(
            @RequestHeader("Authorization") String token,
            @RequestPart("avatar") MultipartFile file) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.updateAvatarByToken(token, file));
    }

    @PutMapping("/@me/enrollments/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDto>> updateEnrollmentByToken(
            @RequestHeader("Authorization") String token,
            @RequestParam Long courseId,
            @RequestParam("enrollment") EnrollmentRequestDto enrollmentDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(enrollmentService.updateEnrollmentByToken(token, courseId, enrollmentDto));
    }

    @DeleteMapping("/@me")
    public ResponseEntity<Void> deleteByToken(@RequestHeader("Authorization") String token) {
        userService.deleteByToken(token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/@me/avatar")
    public ResponseEntity<Void> deleteAvatarByToken(@RequestHeader("Authorization") String token) {
        userService.deleteAvatarByToken(token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/@me/enrollments/{courseId}")
    public ResponseEntity<List<EnrollmentResponseDto>> deleteEnrollmentByToken(
            @RequestHeader("Authorization") String token,
            @PathVariable Long courseId) {
        enrollmentService.deleteEnrollmentByToken(token, courseId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

}
