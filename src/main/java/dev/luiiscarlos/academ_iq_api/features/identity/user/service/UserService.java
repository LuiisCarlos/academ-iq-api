package dev.luiiscarlos.academ_iq_api.features.identity.user.service;

import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.file.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.features.file.exception.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdatePasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;

public interface UserService {

    /**
     * Retrieves the user's full information by ID
     *
     * @param userId the ID of the user
     * @return the user response
     */
    UserResponse get(long userId);

    /**
     * Retrieves the user's avatar file
     *
     * @param userId the ID of the user
     * @return the avatar file response
     */
    FileResponse getAvatar(long userId);

    /**
     * Updates user information
     *
     * @param userId  the ID of the user
     * @param request the data to update
     * @return the updated user response
     */
    UserResponse update(long userId, UpdateRequest request);

    /**
     * Replaces or updates the user's avatar
     *
     * @param userId        the ID of the user
     * @param multipartFile the new avatar file
     * @return the uploaded avatar file response
     * @throws FileStorageException     if the user does not exist
     * @throws InvalidFileTypeException if the content type of the file is invalid
     */
    FileResponse patchAvatar(long userId, MultipartFile multipartFile);

    /**
     * Updates the user's password
     *
     * @param userId  the ID of the user
     * @param request the request containing the new password
     */
    void updatePassword(long userId, UpdatePasswordRequest request);

    /**
     * Deletes the user account
     *
     * @param userId the ID of the user
     */
    void delete(long userId);

    /**
     * Deletes the user's avatar file
     *
     * @param userId the ID of the user
     */
    void deleteAvatar(long userId);

}