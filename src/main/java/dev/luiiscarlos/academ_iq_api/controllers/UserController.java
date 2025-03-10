package dev.luiiscarlos.academ_iq_api.controllers;

import java.util.List;

import org.springframework.core.io.Resource;
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

import dev.luiiscarlos.academ_iq_api.dtos.UserResponseDto;
import dev.luiiscarlos.academ_iq_api.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.services.StorageService;
import dev.luiiscarlos.academ_iq_api.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final StorageService storageService;

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
    public ResponseEntity<Resource> findAvatarById(@PathVariable Long id, HttpServletRequest request) {
        Resource avatar = userService.findAvatarById(id);

        String contentType = storageService.checkContentType(request, avatar);

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(avatar);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userService.updateById(id, user));
    }

    @PutMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> updateAvatarById(HttpServletRequest request, @PathVariable Long id, @RequestPart("avatar") MultipartFile file) {

        Resource avatar = userService.updateAvatarById(id, file);

        String contentType = storageService.checkContentType(request, avatar);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(contentType))
                .body(avatar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        userService.deleteAvatarById(id);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @GetMapping("/@me")
    public ResponseEntity<UserResponseDto> findMe(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userMapper.mapToUserResponseDto(userService.findByToken(token)));
    }

    @GetMapping("/@me/avatar")
    public ResponseEntity<Resource> findMyAvatar(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        Resource avatar = userService.findAvatarByToken(token);

        String contentType = storageService.checkContentType(request, avatar);

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(avatar);
    }

    @PutMapping("/@me")
    public ResponseEntity<UserResponseDto> updateMe(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userMapper.mapToUserResponseDto(userService.updateByToken(token)));
    }

    @PutMapping(value = "/@me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> updateMyAvatar(HttpServletRequest request, @RequestPart("avatar") MultipartFile file) {
        String token = request.getHeader("Authorization");

        Resource avatar = userService.updateAvatarByToken(token, file);

        String contentType = storageService.checkContentType(request, avatar);

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .body(avatar);
    }

    @DeleteMapping("/@me")
    public ResponseEntity<Void> deleteMe(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        userService.deleteByToken(token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @DeleteMapping("/@me/avatar")
    public ResponseEntity<Void> deleteMyAvatar(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        userService.deleteAvatarByToken(token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }
}
