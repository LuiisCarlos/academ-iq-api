package dev.luiiscarlos.academ_iq_api.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import dev.luiiscarlos.academ_iq_api.exceptions.FileStorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.repositories.FileRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FileServiceImpl {

	public static final String[] ALLOWED_IMAGE_TYPES = new String[] { "image/jpeg", "image/png" };

	public static final String[] ALLOWED_VIDEO_TYPES = new String[] { "video/mp4", "video/avi", "video/mkv" };

	private final FileRepository fileRepository;

	private final Cloudinary cloudinary;

	/**
	 * Retrieves all files
	 *
	 * @return the list of files
	 *
	 * @throws FileNotFoundException if the files can not be found
	 */
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
	public File findByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to find file: File not found with name " + filename));

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
	public File findByFilename(String token, String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to find file: File not found with name " + filename));

		if ((token == null || token.isEmpty()) && !file.isImage())
			throw new InvalidCredentialsException("Failed to find file: Access denied");

		return file;
	}

	/**
	 * Retrieves a Cloudinary resource URL by its filename (public_id)
	 *
	 * @param filename the public_id of the file in Cloudinary
	 *
	 * @return the Resource (URL) pointing to Cloudinary's CDN
	 *
	 * @throws FileNotFoundException if the file doesn't exist in the database
	 */
	public Resource findResourceByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to find resource: File not found with name " + filename));

		try {
			String fileUrl;

			if (file.isImage()) {
				fileUrl = cloudinary.url()
						.resourceType("image")
						.format(file.getExtension())
						.generate(filename);
			} else {
				fileUrl = cloudinary.url()
						.resourceType("raw")
						.generate(filename);
			}

			return new UrlResource(new URL(fileUrl));
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("Failed to generate Cloudinary URL: " + e.getMessage());
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
	public File save(MultipartFile file, boolean isImage) {
		if (file.isEmpty() || file == null)
			throw new FileStorageException("Failed to save file: File is required");

		try {
			Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
					"resource_type",
					isImage ? "image" : "raw",
					"folder",
					"uploads"));

			String publicId = (String) uploadResult.get("public_id");
			String secureUrl = (String) uploadResult.get("secure_url");
			String originalFilename = file.getOriginalFilename();
			String fileNameOnly = publicId.contains("/")
					? publicId.substring(publicId.lastIndexOf("/") + 1)
					: publicId;

			File fileEntity = File.builder()
					.filename(fileNameOnly)
					.contentType(file.getContentType())
					.size(file.getSize())
					.isImage(isImage)
					.url(secureUrl)
					.extension(StringUtils.getFilenameExtension(originalFilename))
					.build();

			return fileRepository.save(fileEntity);
		} catch (IOException e) {
			throw new FileStorageException("Failed to upload file to Cloudinary: " + e.getMessage());
		}
	}

	/**
	 * Deletes a file from Cloudinary and the database by its filename (public_id in
	 * Cloudinary)
	 *
	 * @param filename the public_id of the file in Cloudinary
	 *
	 * @throws FileNotFoundException if the file doesn't exist in the database
	 * @throws FileStorageException  if the file can't be deleted from Cloudinary
	 */
	public void deleteByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						"Failed to delete file: File not found with name " + filename));

		if (!file.isDefaultFile()) {
			try {
				Map<?, ?> result = cloudinary.uploader().destroy("uploads/" + filename, ObjectUtils.asMap(
						"resource_type",
						file.isImage() ? "image" : "raw",
						"invalidate",
						true));

				if (!"ok".equals(result.get("result")))
					throw new FileStorageException("Cloudinary deletion failed for file: " + filename);

				fileRepository.deleteByFilename(filename);
			} catch (IOException e) {
				throw new FileStorageException("Failed to delete file from Cloudinary: " + e.getMessage());
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
