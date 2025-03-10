package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserChangePasswordDto;
import dev.luiiscarlos.academ_iq_api.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody String token) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(authService.refresh(token));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto loginRequest) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@RequestBody UserRegisterRequestDto registerRequest) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(authService.register(registerRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, @RequestBody String token) {
        authService.logout(request, response, token);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody UserChangePasswordDto changePassword) {
        authService.changePassword(changePassword);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .build();
    }
}
