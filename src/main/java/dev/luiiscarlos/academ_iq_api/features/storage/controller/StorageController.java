package dev.luiiscarlos.academ_iq_api.features.storage.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class StorageController {

	private final StorageService storageService;

	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> findResourceByFilename(
			@PathVariable String filename,
			@RequestHeader(value = "Authorization", required = false) String token) {
		Resource resource = storageService.getResource(filename);
		File file = storageService.get(filename);

		String contentType = file.getContentType();

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}

}
