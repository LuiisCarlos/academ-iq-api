package dev.luiiscarlos.academ_iq_api.features.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.Credentials;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.ResetPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestParam String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.refresh(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestHeader("Origin") String origin,
            @RequestBody Credentials credentials) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.login(credentials, origin));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestHeader("Origin") String origin,
            @Valid @RequestBody RegisterRequest userDto) {


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.register(userDto, origin));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam String token) {
        authService.verify(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/recover-password")
    public ResponseEntity<Void> recoverPassword(
            @RequestHeader("Origin") String origin,
            @RequestBody String email) {
        authService.recoverPassword(origin, email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody ResetPasswordRequest userDto) {
        authService.resetPassword(token, userDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
