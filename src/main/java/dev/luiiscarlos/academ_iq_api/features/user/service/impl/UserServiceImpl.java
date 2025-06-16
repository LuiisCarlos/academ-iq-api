package dev.luiiscarlos.academ_iq_api.features.user.service.impl;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.file.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.features.file.exception.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.features.file.mapper.FileMapper;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.features.user.dto.UpdatePasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.user.dto.UpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.features.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.user.service.UserService;
import dev.luiiscarlos.academ_iq_api.shared.enums.FileType;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserQueryService userQueryService;

    private final UserMapper userMapper;

    private final FileService fileService;

    private final FileMapper fileMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponse get(long userId) {
        User user = userQueryService.findById(userId);

        return userMapper.toUserResponse(user);
    }

    public FileResponse getAvatar(long userId) {
        User user = userQueryService.findById(userId);

        return fileMapper.toFileResponseDto(user.getAvatar());
    }

    public UserResponse update(long userId, UpdateRequest request) {
        User user = userQueryService.findById(userId);

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstname(user.getFirstname());
        user.setLastname(request.getLastname());
        user.setBirthdate(request.getBirthdate());
        user.setPhone(request.getPhone());
        user.setBiography(request.getBiography());
        user.setDni(request.getDni());
        user.setGithubUrl(request.getGithubUrl());
        user.setLinkedinUrl(request.getLinkedinUrl());
        user.setWebsiteUrl(request.getWebsiteUrl());
        user.setStudies(request.getStudies());
        user.setJobArea(request.getJobArea());
        user.setWorkExperience(request.getWorkExperience());
        user.setCompanyName(request.getCompanyName());
        user.setManager(request.isManager());
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toUserResponse(userQueryService.save(user));
    }

    public FileResponse patchAvatar(long userId, MultipartFile multipartFile) {
        User user = userQueryService.findById(userId);

        if (multipartFile.isEmpty())
            throw new FileStorageException("Avatar is required");
        if (!fileService.isValidImage(multipartFile))
            throw new InvalidFileTypeException("Invalid file content type");

        fileService.get(user.getAvatar().getFilename());

        File avatar = fileService.create(multipartFile, FileType.AVATAR);
        avatar.setUpdatedAt(LocalDateTime.now());

        user.setAvatar(avatar);
        userQueryService.save(user);

        return fileMapper.toFileResponseDto(userQueryService.save(user).getAvatar());
    }

    public void updatePassword(long userId, UpdatePasswordRequest request) {
        User user = userQueryService.findById(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword().substring(8)))
            throw new UserWithDifferentPasswordsException("Invalid old password");

        if (request.getPassword().equals(request.getCurrentPassword()))
            throw new UserWithDifferentPasswordsException("New password is the same as the old password");

        user.setPassword("{bcrypt}" + passwordEncoder.encode(request.getPassword()));
        userQueryService.save(user);
    }

    public void delete(long userId) {
        User user = userQueryService.findById(userId);

        fileService.delete(user.getAvatar().getFilename());
        user.setAvatar(fileService.get("default-user-avatar_nsfvaz"));

        userQueryService.delete(user);
    }

    public void deleteAvatar(long userId) {
        User user = userQueryService.findById(userId);

        fileService.delete(user.getAvatar().getFilename());
        user.setAvatar(fileService.get("default-user-avatar_nsfvaz"));

        userQueryService.save(user);
    }

}
