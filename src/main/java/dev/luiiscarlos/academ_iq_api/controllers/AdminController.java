package dev.luiiscarlos.academ_iq_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.services.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(
            @PathVariable("id") Long userId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findById(userId));
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<FileResponseDto> findAvatarById(
            @PathVariable("id") Long userId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findAvatarById(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateById(
            @PathVariable("id") Long userId,
            @RequestBody User user) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.updateById(userId, user));
    }

    @PutMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> updateAvatarById(
            @PathVariable("id") Long userId,
            @RequestPart("avatar") MultipartFile file) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.patchAvatarById(userId, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("id") Long userId) {
        userService.deleteById(userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteAvatarById(
            @PathVariable("id") Long userId) {
        userService.deleteAvatarById(userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

}