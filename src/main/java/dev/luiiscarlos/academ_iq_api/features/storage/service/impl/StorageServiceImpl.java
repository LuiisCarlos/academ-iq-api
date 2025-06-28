package dev.luiiscarlos.academ_iq_api.features.storage.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.storage.exception.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.storage.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.storage.model.FileType;
import dev.luiiscarlos.academ_iq_api.features.storage.repository.FileRepository;
import dev.luiiscarlos.academ_iq_api.features.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

	private final FileRepository fileRepository;

	private final Cloudinary cloudinary;

	public File create(MultipartFile multipartFile, FileType fileType) {
		boolean image = !fileType.equals(FileType.COURSE_VIDEO);

		if (Objects.isNull(multipartFile) || multipartFile.isEmpty())
			throw new FileStorageException(ErrorMessages.FILE_MISSING);

		if (image && multipartFile.getSize() > 10_000_000)
			throw new FileStorageException(String.format(ErrorMessages.FILE_TOO_LARGE, 10));

		String originalFilename = multipartFile.getOriginalFilename();
		String extension = StringUtils.getFilenameExtension(originalFilename);

		try {
			Map<String, Object> params = new HashMap<>();
			params.put("resource_type", image ? "image" : "video");
			params.put("folder", image ? "uploads/images" : "uploads/videos");
			params.put("type", fileType);

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

			if (image) {
				Double duration = (double) response.get("duration");
				file.setDuration(Duration.ofMillis((long) (duration * 1000)));
			}

			return fileRepository.save(file);
		} catch (IOException e) {
			throw new FileStorageException(
					String.format(ErrorMessages.FILE_UPLOAD_CLOUDINARY_BY_NAME, originalFilename));
		}
	}

	public List<File> getAll() {
		List<File> files = fileRepository.findAll();

		if (files.isEmpty() || Objects.isNull(files))
			throw new FileNotFoundException(ErrorMessages.FILES_NOT_FOUND);

		return files;
	}

	public File get(long fileId) {
		return fileRepository.findById(fileId)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND, fileId)));
	}

	public File get(String filename) {
		File file = fileRepository.findByFilename(filename)
				.orElseThrow(() -> new FileNotFoundException(
						String.format(ErrorMessages.FILE_NOT_FOUND_BY_NAME, filename)));

		return file;
	}

	public Resource getResource(String filename) {
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
			throw new FileNotFoundException(
					String.format(ErrorMessages.RESOURCE_GENERATION_BY_NAME, filename));
		}
	}

	public void delete(String filename) {
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
				throw new FileStorageException(String.format(ErrorMessages.FILE_DELETION_CLOUDINARY, filename));

			fileRepository.deleteByFilename(filename);
		} catch (IOException e) {
			throw new FileStorageException(String.format(ErrorMessages.FILE_DELETION_CLOUDINARY_BY_NAME, filename));
		}
	}

	public boolean validateImage(MultipartFile multiPartfile) {
		if (multiPartfile.isEmpty() || multiPartfile == null)
			throw new FileStorageException(ErrorMessages.FILE_MISSING);

		if (!Arrays.asList(ALLOWED_IMAGE_TYPES).contains(multiPartfile.getContentType()))
			throw new FileStorageException(
					String.format(ErrorMessages.INVALID_CONTENT_TYPE, multiPartfile.getContentType()));

		return true;
	}

	public boolean validateVideo(MultipartFile multiPartfile) {
		if (multiPartfile.isEmpty() || Objects.isNull(multiPartfile))
			throw new FileStorageException(ErrorMessages.FILE_MISSING);

		if (!Arrays.asList(ALLOWED_VIDEO_TYPES).contains(multiPartfile.getContentType()))
			throw new FileStorageException(
					String.format(ErrorMessages.INVALID_CONTENT_TYPE, multiPartfile.getContentType()));

		return true;
	}

}
