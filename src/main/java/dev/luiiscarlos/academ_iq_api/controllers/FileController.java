package dev.luiiscarlos.academ_iq_api.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.services.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileController {

	private final FileService fileService;

	/**
	 * Endpoint to get a file by its filename.
	 *
	 * @param filename the name of the file to be retrieved
	 * @param token    the authorization token (optional)
	 * @return the file as a Resource wrapped in a ResponseEntity
	 */
	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> findResourceByFilename(
			@PathVariable String filename,
			@RequestHeader(value = "Authorization", required = false) String token) {
		Resource resource = fileService.findResourceByFilename(filename);
		File file = fileService.findByFilename(filename);

		String contentType = file.getContentType();

		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}

}
