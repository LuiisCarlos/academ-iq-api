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
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.controllers.FileController;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.services.interfaces.FileService;
import dev.luiiscarlos.academ_iq_api.repositories.FileRepository;

import jakarta.transaction.Transactional;

//import lombok.RequiredArgsConstructor;

@Service
@Transactional
public class FileServiceImpl implements FileService {

	public static final String[] ALLOWED_IMAGE_TYPES = new String[] {"image/jpeg", "image/png"};

	public static final String[] ALLOWED_VIDEO_TYPES = new String[] {"video/mp4", "video/avi", "video/mkv"};

	private final Path ROOT_LOCATION;

	@Autowired
	private final FileRepository fileRepository;

	public FileServiceImpl(
		@Value("${storage.root-location}") String rootLocation,
		FileRepository fileRepository
	) {
		this.ROOT_LOCATION = Paths.get(rootLocation);
		this.fileRepository = fileRepository;
	}


	@Override
	public void init() {
		try {
            Files.createDirectories(ROOT_LOCATION);
        } catch (IOException e) {
            throw new FileStorageException("Storage not initialized");
        }
	}

	@Override
	public List<File> findAll() {
		List<File> files = fileRepository.findAll();

		if (fileRepository.findAll().isEmpty() || fileRepository.findAll() == null)
			throw new FileNotFoundException("Failed to find files: No files found");

		return files;
	}

	@Override
	public File findById(Long id) {
		return fileRepository.findById(id)
			.orElseThrow(() -> new FileNotFoundException("Failed to find file: File not found with id " + id));
	}

	@Override
	public File findByFilename(String filename) {
		File file = fileRepository.findByFilename(filename)
			.orElseThrow(() -> new FileNotFoundException("Failed to find file: File not found with name " + filename));

		if (!file.isImage()) //TODO: return video if authenticated else return bad request
			throw new InvalidFileTypeException("Failed to find file: Files with video content type can not be retrieve");

		return file;
	}

	@Override
	public Resource findResourceByFilename(String filename) {
		try {
            Path filePath = ROOT_LOCATION.resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable())
				throw new FileNotFoundException("Failed to find resource: File not found with name " + filename);

			return resource;
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Failed to find resource: " + e.getMessage());
        }
	}

	@Override
	@SuppressWarnings("null") // TODO: Review this
	public File save(MultipartFile file, boolean isImage) {
		if (file.isEmpty() || file == null) throw new FileStorageException("Failed to save file: File is required");

        try (InputStream inputStream = file.getInputStream()) {
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
        	String storedFilename = System.currentTimeMillis() + "_" + filename;
            if (filename.contains(".."))
                throw new FileStorageException(
					"Failed to save file: File with relative path outside current directory"); // Change this msg

            Files.copy(inputStream, ROOT_LOCATION.resolve(storedFilename),
                StandardCopyOption.REPLACE_EXISTING);

			String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();

			String urlFile = MvcUriComponentsBuilder
				.fromMethodName(FileController.class, "findResourceByFilename", storedFilename)
				.build()
				.toUriString();

			File fileEntitiy = File.builder()
				.filename(storedFilename)
				.contentType(contentType)
				.size(file.getSize())
				.isImage(isImage)
				.url(urlFile)
				.extension(StringUtils.getFilenameExtension(filename))
				.build();

			return fileRepository.save(fileEntitiy);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + e.getMessage());
        }
	}

	@Override
	public void deleteByFilename(String filename) {
		if (!fileRepository.existsByFilename(filename))
			throw new FileNotFoundException("Failed to delete file: File not found with name " + filename);

		try {
			File file = fileRepository.findByFilename(filename).get();
			if (!file.isDefaultFile()) {
				fileRepository.deleteByFilename(filename);
				Path filePath = ROOT_LOCATION.resolve(filename);
				Files.deleteIfExists(filePath);
			}
		} catch (IOException e) {
			throw new FileStorageException("Failed to delete file: " + e.getMessage());
		}
	}

	public boolean validateImage(MultipartFile image) {
        String contentType = image.getContentType();
        if (!Arrays.asList(ALLOWED_IMAGE_TYPES).contains(contentType))
            return false;
        return true;
    }

	public boolean validateVideo(MultipartFile video) {
        String contentType = video.getContentType();
        if (!Arrays.asList(ALLOWED_VIDEO_TYPES).contains(contentType))
			return false;
        return true;
    }

}
