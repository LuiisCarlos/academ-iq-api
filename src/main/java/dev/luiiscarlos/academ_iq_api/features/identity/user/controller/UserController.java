package dev.luiiscarlos.academ_iq_api.features.identity.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.PasswordUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserService;
import dev.luiiscarlos.academ_iq_api.features.storage.dto.FileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/@me")
    public ResponseEntity<UserResponse> get(@AuthenticationPrincipal Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.get(userId));
    }

    @PutMapping("/@me")
    public ResponseEntity<UserResponse> update(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.update(userId, request));
    }

    @PutMapping("/@me/change-password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/@me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> updateAvatar(
            @AuthenticationPrincipal Long userId,
            @RequestPart("avatar") MultipartFile multipartFile) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.patchAvatar(userId, multipartFile));
    }

    @DeleteMapping("/@me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId) {
        userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/@me/avatar")
    public ResponseEntity<Void> deleteAvatar(@AuthenticationPrincipal Long userId) {
        userService.deleteAvatar(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
