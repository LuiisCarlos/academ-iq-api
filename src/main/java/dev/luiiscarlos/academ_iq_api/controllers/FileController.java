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
import dev.luiiscarlos.academ_iq_api.services.FileServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

	private final FileServiceImpl fileService;

	/**
	 * Endpoint to get a file by its filename.
	 *
	 * @param filename the name of the file to be retrieved
	 * @param token the authorization token (optional)
	 * @return the file as a Resource wrapped in a ResponseEntity
	 */
	@SuppressWarnings("null")// <- Already handled
	@GetMapping("/{filename:.+}")
	public ResponseEntity<Resource> findResourceByFilename(
			@PathVariable String filename,
			@RequestHeader(value = "Authorization", required = false) String token) {
		Resource resource = fileService.findResourceByFilename(filename);
		File file;
		file = fileService.findByFilename(filename);
		/* if (token == null || token.isEmpty())
		else
			file = fileService.findByFilename(token, filename); */
		String contentType = file.getContentType();

		return ResponseEntity
            .status(HttpStatus.OK)
			.contentType(MediaType.parseMediaType(contentType))
			.body(resource);
	}

}
