package dev.luiiscarlos.academ_iq_api.user.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.auth.exception.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.auth.security.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.file.file.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.user.dto.UpdatePassword;
import dev.luiiscarlos.academ_iq_api.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.user.model.User;

public interface UserService {

    /**
     * Saves the user
     *
     * @param user the new user
     * @return user the user
     * @throws UserNotFoundException if the user does not exist
     */
    User save(User user);

    /**
     * Finds all users
     *
     * @return the list of users
     * @throws UserNotFoundException if the user does not exist
     */
    List<User> findAll();

    /**
     * Finds the user by its id
     *
     * @param id the id of the user
     * @return the user
     * @throws UserNotFoundException if the user does not exist
     */
    User findById(Long id);

    /**
     * Finds the user by its username
     *
     * @param username the user name
     * @return the user
     * @throws UserNotFoundException if the user does not exist
     */
    User findByUsername(String username);

    /**
     * Finds the user by its email
     *
     * @param email The user's email
     * @return The user
     * @throws UserNotFoundException if the user does not exist
     */
    User findByEmail(String email);

    /**
     * Finds the user's avatar by its id
     *
     * @param id the user's id
     * @return {@link FileResponseDto} the user's avatar
     * @throws UserNotFoundException if the user does not exist
     */
    FileResponseDto findAvatarById(Long id);

    /**
     * Finds the user by the token
     *
     * @param token the user's token
     * @return the user
     * @throws AuthCredentialsNotFoundException if the token does not exist
     * @throws InvalidCredentialsException      if the token is invalid
     * @throws InvalidTokenException            if the token is expired
     * @throws UserNotFoundException            if the user does not exist
     */
    User findByToken(String token);

    /**
     * Finds the user's avatar by the token
     *
     * @param token the user's token
     * @return the user's avatar
     */
    FileResponseDto findAvatarByToken(String token);

    /**
     * Updates the user's information by its id
     *
     * @param userId the id of the user
     * @param user   the new user
     * @return the updated user
     * @throws UserNotFoundException if the user does not exist
     */
    User updateById(Long id, User user);

    /**
     * Updates the user's password by the token
     *
     * @param token       the user's token
     * @param passwordDto the current and new password
     */
    void updatePasswordByToken(String token, UpdatePassword passwordDto);

    /**
     * Updates the user's information by the token
     *
     * @param token   the user's token
     * @param userDto the user's token
     * @return the updated user
     */
    User updateByToken(String token, User user);

    /**
     * Updates the user's avatar by its id
     *
     * @param userId the id of the user
     * @param avatar the new avatar
     * @return the updated avatar
     * @throws UserNotFoundException if the user does not exist
     */
    FileResponseDto patchAvatarById(Long id, MultipartFile avatar);

    /**
     * Updates the user's avatar by the token
     *
     * @param token  the user's token
     * @param avatar the new avatar
     * @return the updated avatar
     */
    FileResponseDto patchAvatarByToken(String token, MultipartFile avatar);

    /**
     * Deletes the user by its id
     *
     * @param userId the id of the user
     * @throws UserNotFoundException if the user does not exist
     */
    void deleteById(Long id);

    /**
     * Deletes the user's avatar by its id
     *
     * @param userId the id of the user
     * @throws UserNotFoundException if the user does not exist
     */
    void deleteAvatarById(Long id);

    /**
     * Deletes the user by the token
     *
     * @param token the user's token
     */
    void deleteByToken(String token);

    /**
     * Deletes the user's avatar by the token
     *
     * @param token the user's token
     */
    void deleteAvatarByToken(String token);

    /**
     * Checks if a user exists by its username
     *
     * @param username the username of the user
     * @return true if the user exists, false otherwise
     */
    Boolean existsByUsername(String username);

}
