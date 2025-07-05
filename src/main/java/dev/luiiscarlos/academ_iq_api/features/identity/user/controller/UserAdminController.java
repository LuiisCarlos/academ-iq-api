package dev.luiiscarlos.academ_iq_api.features.identity.user.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.*;
import dev.luiiscarlos.academ_iq_api.features.identity.user.facade.UserFacade;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.dto.*;
import dev.luiiscarlos.academ_iq_api.features.storage.dto.FileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class UserAdminController {

    private final UserFacade userFacade;

    @PostMapping("/users/{id}/assign-role")
    public ResponseEntity<Void> assignRole(
            @PathVariable("id") long userId,
            @RequestBody RoleRequest request) {
        userFacade.assignRole(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/{id}/remove-role")
    public ResponseEntity<Void> removeRole(
            @PathVariable("id") long userId,
            @RequestBody RoleRequest request) {
        userFacade.removeRole(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/{id}/set-roles")
    public ResponseEntity<Void> removeRole(
            @PathVariable("id") long userId,
            @RequestBody RolesRequest request) {
        userFacade.setRoles(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/{id}/force-logout")
    public ResponseEntity<Void> forceLogout(@PathVariable("id") long userId) {
        userFacade.forceLogout(userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAll(Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.getAll(pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable("id") long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.get(userId));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable("id") long userId,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.update(userId, request));
    }

    @PutMapping("/users/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable("id") long userId,
            @RequestBody AdminPasswordRequest request) {
        userFacade.changePassword(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/@me/update-password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserPasswordUpdateRequest request) {
        userFacade.updatePassword(userId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/users/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponse> updateAvatar(
            @PathVariable("id") long userId,
            @RequestPart("avatar") MultipartFile multipartFile) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFacade.patchAvatar(userId, multipartFile));
    }

    @PatchMapping("/users/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable("id") long userId) {
        userFacade.activate(userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable("id") long userId) {
        userFacade.deactivate(userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long userId) {
        userFacade.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/users/{id}/avatar")
    public ResponseEntity<Void> deleteAvatar(@PathVariable("id") long userId) {
        userFacade.deleteAvatar(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
