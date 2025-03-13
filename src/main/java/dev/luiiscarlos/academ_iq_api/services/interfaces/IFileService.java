package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.models.File;

public interface IFileService {

    void init();

	List<File> findAll();

    File findById(Long id);

    File findByFilename(String filename);

    Resource findResourceByFilename(String filename);

	File save(MultipartFile file, boolean isImage);

	void deleteByFilename(String filename);

}
