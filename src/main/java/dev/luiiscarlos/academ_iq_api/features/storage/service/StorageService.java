package dev.luiiscarlos.academ_iq_api.features.storage.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.storage.exception.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.storage.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.storage.model.FileType;

public interface StorageService {

    public static final String[] ALLOWED_IMAGE_TYPES = { "image/jpeg", "image/png" };

    public static final String[] ALLOWED_VIDEO_TYPES = { "video/mp4", "video/avi", "video/mkv" };

    /**
     * Saves a file to Cloudinary and the database
     *
     * @param multipartFile the file to be saved
     * @param fileType      {@link FileType} the type (e.g. AVATAR, THUMBNAIL,
     *                      COURSE_VIDEO)
     * @return {@link File} the file entity saved in the database
     * @throws FileStorageException if the file can not be saved
     */
    File create(MultipartFile multipartFile, FileType fileType);

    /**
     * Retrieves all files from the database
     *
     * @return A list of {@link File} entities
     * @throws FileNotFoundException if the files can not be found
     */
    List<File> getAll();

    /**
     * Retrieves a file by its id
     *
     * @param fileId the ID of the file
     * @return {@link File} the file entity
     * @throws FileNotFoundException if the file can not be found
     */
    File get(long fileId);

    /**
     * Retrieves a file by its filename
     *
     * @param filename the name of the file without extension
     * @return {@link File} the file
     * @throws FileNotFoundException if the file can not be found
     */
    File get(String filename);

    /**
     * Retrieves a resource URL from Cludinary by its filename (public_id)
     *
     * @param filename the public_id of the file in Cloudinary
     * @return the Resource (URL) pointing to Cloudinary's CDN
     * @throws FileNotFoundException if the file doesn't exist in the database
     */
    Resource getResource(String filename);

    /**
     * Deletes a file from Cloudinary and the database by its filename (public_id in
     * Cloudinary)
     *
     * @param filename the public_id of the file in Cloudinary
     * @throws FileNotFoundException if the file doesn't exist in the database
     * @throws FileStorageException  if the file can't be deleted from Cloudinary
     */
    void delete(String filename);

    /**
     * Validates if the given file is valid image type and not empty
     *
     * @param multipartFile the file to be validated
     * @return true if the file is valid, false otherwise
     * @throws FileStorageException if the file is not a valid image type or is empty
     */
    boolean validateImage(MultipartFile multipartFile);

    /**
     * Validates if the given file is valid video type and not empty
     *
     * @param multipartFile the file to be validated
     * @return true if the file is valid, false otherwise
     * @throws FileStorageException if the file is not a valid video type or is empty
     */
    boolean validateVideo(MultipartFile multipartFile);

}
