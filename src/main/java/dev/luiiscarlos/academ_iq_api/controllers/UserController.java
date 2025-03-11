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
import dev.luiiscarlos.academ_iq_api.models.dtos.UserResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping()
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findById(id));
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<FileResponseDto> findAvatarById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findAvatarById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateById(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.updateById(id, user));
    }

    @PutMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> updateAvatarById(HttpServletRequest request, @PathVariable Long id, @RequestPart("avatar") MultipartFile file) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.updateAvatarById(id, file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteAvatarById(@PathVariable Long id) {
        userService.deleteAvatarById(id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @GetMapping("/@me")
    public ResponseEntity<UserResponseDto> findByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userMapper.toUserResponseDto(userService.findByToken(token)));
    }

    @GetMapping("/@me/avatar")
    public ResponseEntity<FileResponseDto> findAvatartByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.findAvatarByToken(token));
    }

    @PutMapping("/@me")
    public ResponseEntity<UserResponseDto> updateByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userMapper.toUserResponseDto(userService.updateByToken(token)));
    }

    @PutMapping(value = "/@me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> updateAvatarByToken(HttpServletRequest request, @RequestPart("avatar") MultipartFile file) {
        String token = request.getHeader("Authorization");

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.updateAvatarByToken(token, file));
    }

    @DeleteMapping("/@me")
    public ResponseEntity<Void> deleteByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        userService.deleteByToken(token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @DeleteMapping("/@me/avatar")
    public ResponseEntity<Void> deleteAvatarByToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        userService.deleteAvatarByToken(token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }
}
