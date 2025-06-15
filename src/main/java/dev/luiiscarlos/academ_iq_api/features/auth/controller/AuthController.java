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
	public ResponseEntity<String> refresh(@RequestBody String refreshToken) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(authService.refresh(refreshToken));
	}

	@GetMapping("/verify")
	public ResponseEntity<Void> verify(@RequestParam String verifyToken) {
		authService.verify(verifyToken);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(
			@Valid @RequestBody RegisterRequest request,
			@RequestHeader("Origin") String origin) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(authService.register(request, origin));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@RequestBody Credentials credentials,
			@RequestHeader("Origin") String origin) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(authService.login(credentials, origin));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody String refreshToken) {
		authService.logout(refreshToken);

		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
	}

	@PostMapping("/recover-password")
	public ResponseEntity<Void> recoverPassword(
			@RequestBody String email,
			@RequestHeader("Origin") String origin) {
		authService.recoverPassword(origin, email);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(
			@Valid @RequestBody ResetPasswordRequest request,
			@RequestParam String recoverToken) {
		authService.resetPassword(recoverToken, request);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

}
