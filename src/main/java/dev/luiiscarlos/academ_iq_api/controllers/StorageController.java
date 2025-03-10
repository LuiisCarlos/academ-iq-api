package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.services.StorageService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/storage")
@RequiredArgsConstructor
public class StorageController { // ? QST: Thinking to delete this controller

	private final StorageService storageService;

	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> findByFilename(@PathVariable String filename, HttpServletRequest request) {
		Resource file = storageService.loadAsResource(filename);

        String contentType = storageService.checkContentType(request, file);

		return ResponseEntity
            .status(HttpStatus.OK)
			.contentType(MediaType.parseMediaType(contentType))
			.body(file);
	}

}
