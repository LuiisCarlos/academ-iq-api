package dev.luiiscarlos.academ_iq_api.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.StorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageFileNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageNoFileTypeFoundException;
import dev.luiiscarlos.academ_iq_api.services.interfaces.IStorageService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StorageService implements IStorageService {

	private final Path rootLocation;

	public StorageService(@Value( "${storage.root-location}" ) String path) {
		this.rootLocation = Paths.get(path);
	}


	@Override
	public void init() {
		try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Storage not initialized");
        }
	}

	@Override
	public String store(MultipartFile file) {
		if (file.isEmpty() || file == null) throw new StorageException("File is required");

		String filename = file.getOriginalFilename();

		if (filename == null) throw new StorageException("File is required");

		filename = StringUtils.cleanPath(filename);
        String extension = StringUtils.getFilenameExtension(filename);
        String justFilename = filename.replace("." + extension, "");
        String storedFilename = System.currentTimeMillis() + "_" + justFilename + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            if (filename.contains("..")) // ! <- This is a security check
                throw new StorageException(
					"Failed to store file with relative path outside current directory");

            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException e) {
            throw new StorageException("Failed to store file");
        }
	}

	@Override
	public Stream<Path> loadAll() {
		try {
            return Files.walk(this.rootLocation, 1)
            		.filter(path -> !path.equals(this.rootLocation))
            		.map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files");
        }
	}

	@Override
	public Path loadByFilename(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
            Path file = loadByFilename(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable())
				throw new StorageFileNotFoundException();

			return resource;
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException();
        }
	}

	@Override
	public void delete(String filename) {
		String justFilename = StringUtils.getFilename(filename);

		try {
			Path file = loadByFilename(justFilename);
			Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new StorageException("Failed to delete a file");
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public String checkContentType(HttpServletRequest request, Resource file) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new StorageNoFileTypeFoundException();
        }

        if (contentType == null) contentType = "application/octet-stream";

        return contentType;
    }

}
