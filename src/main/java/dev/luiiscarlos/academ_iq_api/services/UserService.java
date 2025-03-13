package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileStorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.FileNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.FileMapper;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.IUserService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final FileService fileService;

    private final TokenService tokenService;

    private final FileMapper fileMapper;

    /**
     * Finds all users
     *
     * @return the list of users
     */
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty())
            throw new UserNotFoundException("Users not found");

        return users;
    }

    /**
     * Finds the user by its id
     *
     * @param id the id of the user
     * @return the user
     */
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Failed to find user: User not found with id " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Failed to find user : User not found with username " + username));
    }

    /**
     * Finds the user's avatar by its id
     *
     * @param id the id of the user
     * @return the user's avatar
     */
    public FileResponseDto findAvatarById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Failed to find user's avatar: User not found with id " + id));

        String filePath = user.getAvatarUrl();

        String filename = StringUtils.getFilename(filePath);
        File file = fileService.findByFilename(filename);

        return fileMapper.toFileResponseDto(file);
    }

    public User save(User user) {
        if (user == null) throw new UserNotFoundException("Failed to save user: User is null");
        return userRepository.save(user);
    }

    /**
     * Updates the user's information by its id
     *
     * @param id the id of the user
     * @param user the new user
     * @return the updated user
     */
    public User updateById(Long id, User user) {
        return userRepository.findById(id).map(u -> {
            u.setUsername(user.getUsername());
            u.setFirstname(user.getFirstname());
            u.setLastname(user.getLastname());
            u.setEmail(user.getEmail());
            u.setPhone(user.getPhone());
            u.setBirthdate(user.getBirthdate());
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException("Failed to update user: User not found with id " + id));
    }

    /**
     * Updates the user's avatar by its id
     *
     * @param id the id of the user
     * @param avatar the new avatar
     * @return the updated avatar
     */
    public FileResponseDto updateAvatarById(Long id, MultipartFile avatar) {
        if (avatar.isEmpty()) throw new FileStorageException("Failed to update user's avatar: Avatar is required");

        File file = fileService.save(avatar, true);

        userRepository.findById(id).map(u -> {
            if (u.getAvatarUrl() != null)
                fileService.deleteByFilename(StringUtils.getFilename(u.getAvatarUrl()));

            u.setAvatarUrl(file.getUrl());
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException("Failed to update user's avatar: User not found with id " + id));

        return fileMapper.toFileResponseDto(file);
    }

    /**
     * Deletes the user by its id
     *
     * @param id the id of the user
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException("Failed to delete user: User not found with id " + id);

        userRepository.deleteById(id);
    }

    /**
     * Deletes the user's avatar by its id
     *
     * @param id the id of the user
     */
    public void deleteAvatarById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Failed to delete user's avatar: User not found with id " + id));

        if (user.getAvatarUrl() == null)
            throw new FileNotFoundException("Failed to delete user's avatar: Avatar not found");

        fileService.deleteByFilename(StringUtils.getFilename(user.getAvatarUrl()));

        userRepository.findById(id).map(u -> {
            u.setAvatarUrl("http://localhost:8888/api/v1/files/default-user-avatar.png"); // TODO: Change this to a Env variable
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException("Failed to delete user's avatar: User not found with id " + id));
    }

    /**
     * Finds the user by the token
     *
     * @param token the token of the user
     * @return the user
     */
    public User findByToken(String token) {
        if (token == null)
            throw new AuthCredentialsNotFoundException("Failed to find user: Token is required");

        token = token.substring(7);

        if (!tokenService.isValidToken(token))
            throw new InvalidCredentialsException("Failed to find user: Invalid token");

        String username = tokenService.extractUsernameFromToken(token);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Failed to find user: User not found with username " + username));
    }

    /**
     * Finds the user's avatar by the token
     *
     * @param token the token of the user
     * @return the user's avatar
     */
    public FileResponseDto findAvatarByToken(String token) {
        User user = this.findByToken(token);

        String filePath = user.getAvatarUrl();

        String filename = StringUtils.getFilename(filePath);
        File file = fileService.findByFilename(filename);

        return fileMapper.toFileResponseDto(file);
    }

    /**
     * Updates the user's information by the token
     *
     * @param token the token of the user
     * @return the updated user
     */
    public User updateByToken(String token) {
        User user = this.findByToken(token);

        return updateById(user.getId(), user);
    }

    /**
     * Updates the user's avatar by the token
     *
     * @param token the token of the user
     * @param avatar the new avatar
     * @return the updated avatar
     */
    public FileResponseDto updateAvatarByToken(String token, MultipartFile avatar) {
        User user = this.findByToken(token);

        return updateAvatarById(user.getId(), avatar);
    }

    /**
     * Deletes the user by the token
     *
     * @param token the token of the user
     */
    public void deleteByToken(String token) {
        User user = this.findByToken(token);

        deleteById(user.getId());
    }

    /**
     * Deletes the user's avatar by the token
     *
     * @param token the token of the user
     */
    public void deleteAvatarByToken(String token) {
        User user = this.findByToken(token);

        deleteAvatarById(user.getId());
    }

    /**
     * Checks if a user exists by its username
     *
     * @param username the username of the user
     * @return true if the user exists, false otherwise
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
