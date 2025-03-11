package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.services.FileService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;

	@SuppressWarnings("null")// <- Already handled in the service layer
	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> findResourceByFilename(@PathVariable String filename, HttpServletRequest request) {
		File file = fileService.findByFilename(filename);
		Resource resource = fileService.findResourceByFilename(filename);

		String contentType = file.getContentType();

		return ResponseEntity
            .status(HttpStatus.OK)
			.contentType(MediaType.parseMediaType(contentType))
			.body(resource);
	}

}

