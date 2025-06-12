package dev.luiiscarlos.academ_iq_api.file;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import dev.luiiscarlos.academ_iq_api.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.file.File;
import dev.luiiscarlos.academ_iq_api.file.exception.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.file.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.file.exception.InvalidFileTypeException;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

	public static final String[] ALLOWED_IMAGE_TYPES = new String[] { "image/jpeg", "image/png" };

	public static final String[] ALLOWED_VIDEO_TYPES = new String[] { "video/mp4", "video/avi", "video/mkv" };

	private final FileRepository fileRepository;

	private final Cloudinary cloudinary;

	/**
	 * Saves a file to Cloudinary and the database
	 *
	 * @param multipartFile the file to be saved
	 * @param type          the type (e.g. avatar, thumbnail, course)
	 * @param image         the resource type (image or video)
	 * @return {@link File} the file entity saved in the database
	 * @throws FileStorageException     if the file can not be saved
	 * @throws InvalidFileTypeException if the resource type is not valid
	 */
	public File save(MultipartFile multipartFile, String type, boolean image) {
		if (multipartFile.isEmpty() || multipartFile == null)
			throw new FileStorageException(ErrorMessages.FILE_MISSING);

		if (image && multipartFile.getSize() > 10_000_000)
			throw new FileStorageException(String.format(ErrorMessages.FILE_TOO_LARGE, 10));

		String originalFilename = multipartFile.getOriginalFilename();
		String extension = StringUtils.getFilenameExtension(originalFilename);

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("resource_type", image ? "image" : "video");
			params.put("folder", image ? "uploads/images" : "uploads/videos");
			//params.put("format", image ? "webp" : "mp4");
			params.put("type", type);
			Map<?, ?> response = cloudinary.uploader().upload(multipartFile.getBytes(), params);

			String publicId = (String) response.get("public_id");
			String secureUrl = (String) response.get("secure_url");
			String filename = publicId.contains("/")
					? publicId.substring(publicId.lastIndexOf("/") + 1)
					: publicId;

			File file = File.builder()
					.filename(filename)
					.contentType(multipartFile.getContentType())
					.size(multipartFile.getSize())
					.image(image)
					.url(secureUrl)
					.extension(extension)
					.build();

			return fileRepository.save(file);
		} catch (IOException e) {
			throw new FileStorageException(String.format(
					ErrorMessages.FILE_UPLOAD_CLOUDINARY_BY_NAME, originalFilename), e);
		}
	}

	/**
	 * Retrieves all files from the database
	 *
	 * @return A list of {@link File} entities
	 * @throws FileNotFoundException if the files can not be found
	 */
	public List<File> findAll() {
		List<File> files = fileRepository.findAll();

		if (fileRepository.findAll().isEmpty() || fileRepository.findAll() == null)
			throw new FileNotFoundException(ErrorMessages.FILES_NOT_FOUND);

		return files;
	}

	/**
	 * Retrieves a file by its id
	 *
	 * @param id the file id
	 * @return {@link File} the file entity
	 * @throws FileNotFoundException if the file can not be found
	 */
	public File findById(Long fileId) {
		return fileRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND, fileId)));
	}

	/**
	 * Retrieves a file by its filename
	 *
	 * @param filename the file name
	 * @return {@link File} the file
	 * @throws FileNotFoundException if the file can not be found
	 */
	public File findByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND_BY_NAME, filename)));

		return file;
	}

	/**
	 * Retrieves a file by its filename
	 *
	 * @param token    the authentication token
	 * @param filename the file name
	 * @return {@link File} the file
	 * @throws FileNotFoundException       if the file can not be found
	 * @throws InvalidCredentialsException if the file can not be retrieved
	 */
	public File findByFilename(String token, String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND_BY_NAME, filename)));

		if ((token == null || token.isEmpty()) && !file.isImage())
			throw new InvalidCredentialsException(
					ErrorMessages.ACCESS_DENIED);

		return file;
	}

	/**
	 * Retrieves a resource URL from Cludinary by its filename (public_id)
	 *
	 * @param filename the public_id of the file in Cloudinary
	 * @return the Resource (URL) pointing to Cloudinary's CDN
	 * @throws FileNotFoundException if the file doesn't exist in the database
	 */
	public Resource findResourceByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND_BY_NAME, filename)));

		String resourceType = file.isImage() ? "image" : "raw";

		try {
			String fileUrl = cloudinary.url()
					.resourceType(resourceType)
					.format(file.getExtension())
					.generate(filename);

			URI uri = URI.create(fileUrl);
			return new UrlResource(uri.toURL());
		} catch (MalformedURLException e) {
			throw new FileNotFoundException( // TODO handle this exception properly
					String.format(ErrorMessages.RESOURCE_GENERATION_BY_NAME, filename), e);
		}
	}

	/**
	 * Deletes a file from Cloudinary and the database by its filename (public_id in
	 * Cloudinary)
	 *
	 * @param filename the public_id of the file in Cloudinary
	 * @throws FileNotFoundException if the file doesn't exist in the database
	 * @throws FileStorageException  if the file can't be deleted from Cloudinary
	 */
	public void deleteByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND_BY_NAME, filename)));

		if (file.isPrimary())
			return;

		String folder = file.isImage() ? "uploads/images/" : "uploads/videos/";

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("resource_type", file.isImage() ? "image" : "video");
			params.put("invalidate", true);

			Map<?, ?> response = cloudinary.uploader().destroy(folder + filename, params);

			if (!"ok".equals(response.get("result")))
				throw new FileStorageException(String.format(
						ErrorMessages.FILE_DELETION_CLOUDINARY, filename));

			fileRepository.deleteByFilename(filename);
		} catch (IOException e) {
			throw new FileStorageException(String.format(
					ErrorMessages.FILE_DELETION_CLOUDINARY_BY_NAME, filename), e);
		}
	}

	/**
	 * Validates if the file is an image
	 *
	 * @param multiPartfile the file to be validated
	 * @return true if the file is an image, false otherwise
	 */
	private boolean isImageContentTypeValid(MultipartFile multiPartfile) {
		return Arrays.asList(ALLOWED_IMAGE_TYPES).contains(multiPartfile.getContentType());
	}

	/**
	 * Validates if the file is a video
	 *
	 * @param multiPartfile the file to be validated
	 * @return true if the file is a video, false otherwise
	 */
	private boolean isVideoContentTypeValid(MultipartFile multiPartfile) {
		return Arrays.asList(ALLOWED_VIDEO_TYPES).contains(multiPartfile.getContentType());
	}

	/**
	 * Validates if the file is a valid image or video
	 *
	 * @param multiPartfile the file to be validated
	 * @return true if the file is a valid image or video, false otherwise
	 */
	public boolean isValidImage(MultipartFile multiPartfile) {
		return isImageContentTypeValid(multiPartfile) && !isVideoContentTypeValid(multiPartfile);
	}

	/**
	 * Validates if the file is a valid video or image
	 *
	 * @param multiPartfile the file to be validated
	 * @return true if the file is a valid video or image, false otherwise
	 */
	public boolean isValidVideo(MultipartFile multiPartfile) {
		return isVideoContentTypeValid(multiPartfile) && !isImageContentTypeValid(multiPartfile);
	}

	/* public Duration findDuration(String filename) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("resource_type", "video");
			Map<?, ?> response = cloudinary.api().resource(filename, params);

			Double duration = (Double) response.get("duration");

			return Duration.ofSeconds(duration.intValue());
		} catch (Exception e) {
			throw new RuntimeException("Bebesita bebe lean");
		}
	} */

}
