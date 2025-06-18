package dev.luiiscarlos.academ_iq_api.features.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

	private final AdminFacade adminFacade;

	@GetMapping("/users")
	public ResponseEntity<Page<UserResponse>> getAll(Pageable pageable) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(adminFacade.getAll(pageable));
	}

}

	/*
	@GetMapping("/users/{id}")
	public ResponseEntity<UserResponse> get(@PathVariable("id") Long userId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(adminFacade.getUser(userId));
	}

	@GetMapping("/users/{id}/avatar")
	public ResponseEntity<FileResponse> getAvatar(@PathVariable("id") Long userId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(userAdminService.getAvatar(userId));
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<UserResponse> update(
			@PathVariable("id") Long userId,
			@RequestBody UpdateRequest request) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(userAdminService.update(userId, request));
	}

	@PutMapping(value = "/users/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FileResponse> patchAvatar(
			@PathVariable("id") Long userId,
			@RequestPart("avatar") MultipartFile multipartFile) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(userAdminService.patchAvatar(userId, multipartFile));
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long userId) {
		userAdminService.delete(userId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping("/users/{id}/avatar")
	public ResponseEntity<Void> deleteAvatar(@PathVariable("id") Long userId) {
		userAdminService.deleteAvatar(userId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

} */