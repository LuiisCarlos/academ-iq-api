package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.dtos.LoginRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.LoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.RegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.RegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequest) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authService.register(registerRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(authService.refreshToken(refreshToken));
    }

}
