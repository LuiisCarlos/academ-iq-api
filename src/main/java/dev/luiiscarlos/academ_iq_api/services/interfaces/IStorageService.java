package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

    void init();

	String store(MultipartFile file);

    Stream<Path> loadAll();

    Path loadByFilename(String filename);

	Resource loadAsResource(String filename);

	void delete(String filename);

	void deleteAll();

}
