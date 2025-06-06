package dev.luiiscarlos.academ_iq_api.controllers;

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

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.Credentials;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordResetDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.LoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.AuthServiceImpl;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    private final UserMapper userMapper;

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestParam String token) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.refresh(token));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestHeader("Origin") String origin,
            @RequestBody Credentials credentials) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.login(credentials, origin));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(
            @RequestHeader("Origin") String origin,
            @Valid @RequestBody UserRegisterRequestDto userDto) {
        User user = userMapper.toModel(userDto);
        UserRegisterResponseDto responseDto = userMapper
                .toRegisterResponseDto(authService.register(user, origin));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseDto);
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
            @RequestBody PasswordResetDto userDto) {
        authService.resetPassword(token, userDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
