package dev.luiiscarlos.academ_iq_api.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.auth.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.auth.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.file.FileStorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.file.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.exceptions.token.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.file.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordUpdateDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.FileMapper;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.UserService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final FileServiceImpl fileService;

    private final TokenServiceImpl tokenService;

    private final FileMapper fileMapper;

    private final PasswordEncoder passwordEncoder;

    /**
     * Saves the user
     *
     * @param user the new user
     *
     * @return user the user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public User save(User user) {
        if (user == null)
            throw new UserNotFoundException("Failed to save user: User is null");

        return userRepository.save(user);
    }

    /**
     * Finds all users
     *
     * @return the list of users
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty())
            throw new UserNotFoundException("Failed to find user: Users not found");

        return users;
    }

    /**
     * Finds the user by its id
     *
     * @param id the id of the user
     *
     * @return the user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user: User not found with id " + userId));
    }

    /**
     * Finds the user by its username
     *
     * @param username the user name
     *
     * @return the user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user : User not found with username " + username));
    }

    /**
     * Finds the user by its email
     *
     * @param email The user's email
     *
     * @return The user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user: User not found with email " + email));
    }

    /**
     * Finds the user's avatar by its id
     *
     * @param id the user's id
     *
     * @return the user's avatar
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    @SuppressWarnings("null")
    public FileResponseDto findAvatarById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user's avatar: User not found with id " + userId));

        File file = fileService.findByFilename(user.getAvatar().getFilename());
        return fileMapper.toFileResponseDto(file);
    }

    /**
     * Updates the user's information by its id
     *
     * @param userId the id of the user
     * @param user   the new user
     *
     * @return the updated user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public User updateById(Long userId, User user) {
        return userRepository.findById(userId).map(u -> {
            u.setUsername(user.getUsername());
            u.setEmail(user.getEmail());
            u.setFirstname(user.getFirstname());
            u.setLastname(user.getLastname());
            u.setBirthdate(user.getBirthdate());
            u.setPhone(user.getPhone());
            u.setBiography(user.getBiography());
            u.setDni(user.getDni());
            u.setGithubUrl(user.getGithubUrl());
            u.setLinkedinUrl(user.getLinkedinUrl());
            u.setWebsiteUrl(user.getWebsiteUrl());
            u.setStudies(user.getStudies());
            u.setJobArea(user.getJobArea());
            u.setWorkExperience(user.getWorkExperience());
            u.setCompanyName(user.getCompanyName());
            u.setIsTeamManager(user.getIsTeamManager());
            u.setWantToUpgrade(user.getWantToUpgrade());
            u.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException(
                "Failed to update user: User not found with id " + userId));
    }

    /**
     * Updates the user's avatar by its id
     *
     * @param userId the id of the user
     * @param avatar the new avatar
     *
     * @return the updated avatar
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    @SuppressWarnings("null")
    public FileResponseDto patchAvatarById(Long userId, MultipartFile avatar) {
        if (avatar.isEmpty())
            throw new FileStorageException(
                    "Failed to update user's avatar: Avatar is required");
        if (!fileService.isValidImage(avatar))
            throw new InvalidFileTypeException(
                    "Failed to update avatar: Invalid file content type");

        User user = userRepository.findById(userId).map(u -> {
            fileService.deleteByFilename(u.getAvatar().getFilename());

            File file = fileService.save(avatar, true);
            file.setUpdatedAt(LocalDateTime.now());
            u.setAvatar(file);

            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException(
                "Failed to update user's avatar: User not found with id " + userId));

        return fileMapper.toFileResponseDto(user.getAvatar());
    }

    /**
     * Deletes the user by its id
     *
     * @param userId the id of the user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException(
                    "Failed to delete user: User not found with id " + userId);

        tokenService.deleteByUserId(userId);
        userRepository.findById(userId).map(u -> {
            fileService.deleteByFilename(u.getAvatar().getFilename());
            return u;
        });
        userRepository.deleteById(userId);
    }

    /**
     * Deletes the user's avatar by its id
     *
     * @param userId the id of the user
     *
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    @SuppressWarnings("null")
    public void deleteAvatarById(Long userId) {
        if (userRepository.existsById(userId))
            throw new UserNotFoundException(
                    "Failed to delete user's avatar: User not found with id " + userId);

        userRepository.findById(userId).map(u -> {
            fileService.deleteByFilename(u.getAvatar().getFilename());

            u.setAvatar(fileService.findByFilename("default-user-avatar.png"));
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException(
                "Failed to delete user's avatar: User not found with id " + userId));
    }

    /**
     * Finds the user by the token
     *
     * @param token the user's token
     *
     * @return the user
     *
     * @throws AuthCredentialsNotFoundException if the token does not exist
     * @throws InvalidCredentialsException      if the token is invalid
     * @throws InvalidTokenException            if the token is expired
     * @throws UserNotFoundException            if the user does not exist
     */
    @Override
    public User findByToken(String token) {
        if (token == null)
            throw new AuthCredentialsNotFoundException("Failed to find user: Token is required");

        token = token.substring(7);

        if (!tokenService.isValidToken(token))
            throw new InvalidCredentialsException("Failed to find user: Invalid token");

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException(
                    "Failed to refresh access token: Refresh token is expired");

        String username = tokenService.getTokenSubject(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user: User not found with username " + username));
    }

    /**
     * Finds the user's avatar by the token
     *
     * @param token the user's token
     *
     * @return the user's avatar
     */
    @Override
    public FileResponseDto findAvatarByToken(String token) {
        User user = this.findByToken(token);
        return fileMapper.toFileResponseDto(user.getAvatar());
    }

    /**
     * Updates the user's information by the token
     *
     * @param token   the user's token
     * @param userDto the user's token
     *
     * @return the updated user
     */
    @Override
    public User updateByToken(String token, User updated) {
        User user = this.findByToken(token);

        return updateById(user.getId(), updated);
    }

    /**
     * Updates the user's avatar by the token
     *
     * @param token  the user's token
     * @param avatar the new avatar
     *
     * @return the updated avatar
     */
    @Override
    public FileResponseDto patchAvatarByToken(String token, MultipartFile avatar) {
        User user = this.findByToken(token);

        return patchAvatarById(user.getId(), avatar);
    }

    /**
     * Updates the user's password by the token
     *
     * @param token       the user's token
     * @param passwordDto the current and new password
     */
    public void updatePasswordByToken(String token, PasswordUpdateDto passwordDto) {
        User user = this.findByToken(token);

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword().substring(8)))
            throw new UserWithDifferentPasswordsException("Failed to update password: Invalid old password");

        if (passwordDto.getNewPassword().equals(passwordDto.getCurrentPassword()))
            throw new UserWithDifferentPasswordsException(
                    "Failed to update password: New password is the same as the old password");

        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword()))
            throw new UserWithDifferentPasswordsException(
                    "Failed to update password: New password does not match the confirm password");

        user.setPassword("{bcrypt}" + passwordEncoder.encode(passwordDto.getNewPassword()));
    }

    /**
     * Deletes the user by the token
     *
     * @param token the user's token
     */
    @Override
    public void deleteByToken(String token) {
        User user = this.findByToken(token);

        deleteById(user.getId());
    }

    /**
     * Deletes the user's avatar by the token
     *
     * @param token the user's token
     */
    @Override
    public void deleteAvatarByToken(String token) {
        User user = this.findByToken(token);

        deleteAvatarById(user.getId());
    }

    /**
     * Checks if a user exists by its username
     *
     * @param username the username of the user
     *
     * @return true if the user exists, false otherwise
     */
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
