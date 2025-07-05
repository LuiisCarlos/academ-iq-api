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

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserPasswordUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.facade.UserFacade;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.storage.dto.FileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/@me")
    public ResponseEntity<UserResponse> get(@AuthenticationPrincipal Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.get(userId));
    }

    @PutMapping("/@me")
    public ResponseEntity<UserResponse> update(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.update(userId, request));
    }

    @PatchMapping("/@me/update-password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserPasswordUpdateRequest request) {
        userFacade.updatePassword(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/@me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> updateAvatar(
            @AuthenticationPrincipal Long userId,
            @RequestPart("avatar") MultipartFile multipartFile) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.patchAvatar(userId, multipartFile));
    }

    @DeleteMapping("/@me")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId) {
        userFacade.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/@me/avatar")
    public ResponseEntity<Void> deleteAvatar(@AuthenticationPrincipal Long userId) {
        userFacade.deleteAvatar(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
