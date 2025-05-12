package dev.luiiscarlos.academ_iq_api.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import dev.luiiscarlos.academ_iq_api.exceptions.FileStorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.controllers.FileController;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.services.interfaces.FileService;
import dev.luiiscarlos.academ_iq_api.repositories.FileRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FileServiceImpl implements FileService {

	public static final String[] ALLOWED_IMAGE_TYPES = new String[] { "image/jpeg", "image/png" };

	public static final String[] ALLOWED_VIDEO_TYPES = new String[] { "video/mp4", "video/avi", "video/mkv" };

	private final Path ROOT_LOCATION;

	@Autowired
	private final FileRepository fileRepository;

	public FileServiceImpl(
			@Value("${storage.root-location}") String rootLocation,
			FileRepository fileRepository) {
		this.ROOT_LOCATION = Paths.get(rootLocation);
		this.fileRepository = fileRepository;
	}

	/**
	 * Initializes the storage location
	 *
	 * @throws IOException          if the storage location can not be created
	 * @throws FileStorageException if the storage location can not be created
	 */
	@Override
	public void init() {
		try {
			Files.createDirectories(ROOT_LOCATION);
		} catch (IOException e) {
			throw new FileStorageException("Storage not initialized");
		}
	}

	/**
	 * Retrieves all files
	 *
	 * @return the list of files
	 *
	 * @throws FileNotFoundException if the files can not be found
	 */
	@Override
	public List<File> findAll() {
		List<File> files = fileRepository.findAll();

		if (fileRepository.findAll().isEmpty() || fileRepository.findAll() == null)
			throw new FileNotFoundException("Failed to find files: No files found");

		return files;
	}

	/**
	 * Retrieves a file by its id
	 *
	 * @param id the file id
	 *
	 * @return the file
	 *
	 * @throws FileNotFoundException if the file can not be found
	 */
	@Override
	public File findById(Long id) {
		return fileRepository.findById(id)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to find file: File not found with id " + id));
	}

	/**
	 * Retrieves a file by its filename
	 *
	 * @param filename the file name
	 *
	 * @return the file
	 *
	 * @throws FileNotFoundException    if the file can not be found
	 * @throws InvalidFileTypeException if the file type is not valid
	 */
	@Override
	public File findByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to find file: File not found with name " + filename));

		/* if (!file.isImage())
			throw new InvalidFileTypeException(
					"Failed to find file: Files with video content type can not be retrieve"); */

		return file;
	}

	/**
	 * Retrieves a file by its filename
	 *
	 * @param token    the authentication token
	 * @param filename the file name
	 *
	 * @return the file
	 *
	 * @throws FileNotFoundException       if the file can not be found
	 * @throws InvalidCredentialsException if the file can not be retrieved
	 */
	@Override
	public File findByFilename(String token, String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to find file: File not found with name " + filename));

		if ((token == null || token.isEmpty()) && !file.isImage())
			throw new InvalidCredentialsException("Failed to find file: Access denied");

		return file;
	}

	/**
	 * Retrieves a resource by its filename
	 *
	 * @param filename the file name
	 *
	 * @return the resource
	 *
	 * @throws FileNotFoundException if the file can not be found
	 */
	@Override
	public Resource findResourceByFilename(String filename) {
		try {
			Path filePath = ROOT_LOCATION.resolve(filename);
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists() || !resource.isReadable())
				throw new FileNotFoundException(
						"Failed to find resource: File not found with name " + filename);

			return resource;
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("Failed to find resource: " + e.getMessage());
		}
	}

	/**
	 * Saves a file
	 *
	 * @param file    the file
	 * @param isImage the file type
	 *
	 * @return the file
	 *
	 * @throws FileStorageException     if the file can not be saved
	 * @throws InvalidFileTypeException if the file type is not valid
	 */
	@Override
	@SuppressWarnings("null")
	public File save(MultipartFile file, boolean isImage) {
		if (file.isEmpty() || file == null)
			throw new FileStorageException("Failed to save file: File is required");

		try (InputStream inputStream = file.getInputStream()) {
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			String storedFilename = System.currentTimeMillis() + "_" + filename;
			if (filename.contains(".."))
				throw new FileStorageException(
						"Failed to save file: File with relative path outside current directory");

			Files.copy(inputStream, ROOT_LOCATION.resolve(storedFilename),
					StandardCopyOption.REPLACE_EXISTING);

			String contentType = file.getContentType() == null
					? "application/octet-stream"
					: file.getContentType();

			String fileUrl = MvcUriComponentsBuilder
					.fromMethodName(FileController.class, "findResourceByFilename", storedFilename, null)
					.build()
					.toUriString();

			File fileEntitiy = File.builder()
					.filename(storedFilename)
					.contentType(contentType)
					.size(file.getSize())
					.isImage(isImage)
					.url(fileUrl)
					.extension(StringUtils.getFilenameExtension(filename))
					.build();

			return fileRepository.save(fileEntitiy);
		} catch (IOException e) {
			throw new FileStorageException("Failed to store file: " + e.getMessage());
		}
	}

	/**
	 * Deletes a file by its filename
	 *
	 * @param filename the file name
	 *
	 * @throws FileNotFoundException if the file can not be deleted
	 * @throws FileStorageException  if the file can not be deleted
	 */
	public void deleteByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to delete file: File not found with name " + filename));

		if (!file.isDefaultFile()) {
			fileRepository.deleteByFilename(filename);
			Path filePath = ROOT_LOCATION.resolve(filename);
			try {
				Files.deleteIfExists(filePath);
			} catch (IOException e) {
				throw new FileStorageException("Failed to delete file: " + e.getMessage());
			}
		}
	}

	/**
	 * Validates if the file is an image
	 *
	 * @param multiPartfile the file to be validated
	 *
	 * @return true if the file is an image, false otherwise
	 */
	private boolean isImageContentTypeValid(MultipartFile multiPartfile) {
		return Arrays.asList(ALLOWED_IMAGE_TYPES).contains(multiPartfile.getContentType());
	}

	/**
	 * Validates if the file is a video
	 *
	 * @param multiPartfile the file to be validated
	 *
	 * @return true if the file is a video, false otherwise
	 */
	private boolean isVideoContentTypeValid(MultipartFile multiPartfile) {
		return Arrays.asList(ALLOWED_VIDEO_TYPES).contains(multiPartfile.getContentType());
	}

	/**
	 * Validates if the file is a valid image or video
	 *
	 * @param multiPartfile the file to be validated
	 *
	 * @return true if the file is a valid image or video, false otherwise
	 */
	public boolean isValidImage(MultipartFile multiPartfile) {
		return isImageContentTypeValid(multiPartfile) && !isVideoContentTypeValid(multiPartfile);
	}

	/**
	 * Validates if the file is a valid video or image
	 *
	 * @param multiPartfile the file to be validated
	 *
	 * @return true if the file is a valid video or image, false otherwise
	 */
	public boolean isValidVideo(MultipartFile multiPartfile) {
		return isVideoContentTypeValid(multiPartfile) && !isImageContentTypeValid(multiPartfile);
	}

}
