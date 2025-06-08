package dev.luiiscarlos.academ_iq_api.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.exceptions.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.exceptions.file.FileStorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.file.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.file.FileResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UpdatePasswordDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.FileMapper;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.UserService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FileService fileService;

    private final TokenServiceImpl tokenService;

    private final UserRepository userRepository;

    private final FileMapper fileMapper;

    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        if (user == null)
            throw new UserNotFoundException("Failed to save user: User is null");

        return userRepository.save(user);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty())
            throw new UserNotFoundException("Failed to find user: Users not found");

        return users;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND , userId)));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND_BY_NAME, username)));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user: User not found with email " + email));
    }

    public FileResponseDto findAvatarById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Failed to find user's avatar: User not found with id " + userId));

        File file = fileService.findByFilename(user.getAvatar().getFilename());
        return fileMapper.toFileResponseDto(file);
    }

    public User updateById(Long userId, User updated) {
        return userRepository.findById(userId).map(u -> {
            u.setUsername(updated.getUsername());
            u.setEmail(updated.getEmail());
            u.setFirstname(updated.getFirstname());
            u.setLastname(updated.getLastname());
            u.setBirthdate(updated.getBirthdate());
            u.setPhone(updated.getPhone());
            u.setBiography(updated.getBiography());
            u.setDni(updated.getDni());
            u.setGithubUrl(updated.getGithubUrl());
            u.setLinkedinUrl(updated.getLinkedinUrl());
            u.setWebsiteUrl(updated.getWebsiteUrl());
            u.setStudies(updated.getStudies());
            u.setJobArea(updated.getJobArea());
            u.setWorkExperience(updated.getWorkExperience());
            u.setCompanyName(updated.getCompanyName());
            u.setManager(updated.isManager());
            u.setWantToUpgrade(updated.getWantToUpgrade());
            u.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, userId)));
    }

    public FileResponseDto patchAvatarById(Long userId, MultipartFile avatar) {
        if (avatar.isEmpty())
            throw new FileStorageException(
                    "Failed to update user's avatar: Avatar is required");
        if (!fileService.isValidImage(avatar))
            throw new InvalidFileTypeException(
                    "Failed to update avatar: Invalid file content type");

        User user = userRepository.findById(userId).map(u -> {
            fileService.deleteByFilename(u.getAvatar().getFilename());

            File file = fileService.save(avatar, "avatar", true);
            file.setUpdatedAt(LocalDateTime.now());
            u.setAvatar(file);

            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException(
                "Failed to update user's avatar: User not found with id " + userId));

        return fileMapper.toFileResponseDto(user.getAvatar());
    }

    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId))
            throw new UserNotFoundException(
                    "Failed to delete user: User not found with id " + userId);

        // tokenService.deleteByUserId(userId);
        userRepository.findById(userId).map(u -> {
            fileService.deleteByFilename(u.getAvatar().getFilename());
            return u;
        });
        userRepository.deleteById(userId);
    }

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

    public User findByToken(String token) {
        tokenService.validate(token, "access");

        token = token.substring(7);
        String username = tokenService.getSubject(token);

        return userRepository.findByToken(token)
                .orElseThrow(() -> new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, username)));
    }

    public FileResponseDto findAvatarByToken(String token) {
        User user = this.findByToken(token);
        return fileMapper.toFileResponseDto(user.getAvatar());
    }

    public User updateByToken(String token, User updated) {
        User user = this.findByToken(token);

        return updateById(user.getId(), updated);
    }

    public FileResponseDto patchAvatarByToken(String token, MultipartFile avatar) {
        User user = this.findByToken(token);

        return patchAvatarById(user.getId(), avatar);
    }

    public void updatePasswordByToken(String token, UpdatePasswordDto passwordDto) {
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

    public void deleteByToken(String token) {
        User user = this.findByToken(token);

        deleteById(user.getId());
    }

    public void deleteAvatarByToken(String token) {
        User user = this.findByToken(token);

        deleteAvatarById(user.getId());
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
