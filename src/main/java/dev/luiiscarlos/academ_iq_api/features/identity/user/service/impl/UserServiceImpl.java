package dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.PasswordUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.exception.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.features.identity.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.UserInfo;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserService;
import dev.luiiscarlos.academ_iq_api.features.storage.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.storage.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.features.storage.exception.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.features.storage.mapper.FileMapper;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;
import dev.luiiscarlos.academ_iq_api.features.storage.model.FileType;
import dev.luiiscarlos.academ_iq_api.features.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserQueryService userQueryService;

    private final UserMapper userMapper;

    private final StorageService storageService;

    private final FileMapper fileMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponse get(long userId) {
        User user = userQueryService.findById(userId);

        return userMapper.toDto(user);
    }

    public FileResponse getAvatar(long userId) {
        User user = userQueryService.findById(userId);

        return fileMapper.toDto(user.getAvatar());
    }

    public UserResponse update(long userId, UserUpdateRequest request) {
        User user = userQueryService.findById(userId);
        UserInfo userInfo = new UserInfo();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstname(user.getFirstname());
        user.setLastname(request.getLastname());
        user.setBirthdate(request.getBirthdate());
        user.setPhone(request.getPhone());
        userInfo.setBiography(request.getBiography());
        userInfo.setDni(request.getDni());
        userInfo.setGithubUrl(request.getGithubUrl());
        userInfo.setLinkedinUrl(request.getLinkedinUrl());
        userInfo.setWebsiteUrl(request.getWebsiteUrl());
        userInfo.setStudies(request.getStudies());
        userInfo.setJobArea(request.getJobArea());
        userInfo.setWorkExperience(request.getWorkExperience());
        userInfo.setCompanyName(request.getCompanyName());
        userInfo.setManager(request.isManager());
        user.setInfo(userInfo);

        return userMapper.toDto(userQueryService.save(user));
    }

    public FileResponse patchAvatar(long userId, MultipartFile multipartFile) {
        User user = userQueryService.findById(userId);

        if (multipartFile.isEmpty())
            throw new FileStorageException("Avatar is required");
        if (!storageService.validateImage(multipartFile))
            throw new InvalidFileTypeException("Invalid file content type");

        storageService.get(user.getAvatar().getFilename());

        File avatar = storageService.create(multipartFile, FileType.AVATAR);

        user.setAvatar(avatar);
        userQueryService.save(user);

        return fileMapper.toDto(userQueryService.save(user).getAvatar());
    }

    public void updatePassword(long userId, PasswordUpdateRequest request) {
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

        storageService.delete(user.getAvatar().getFilename());
        user.setAvatar(storageService.get("default-user-avatar_nsfvaz"));

        userQueryService.delete(user);
    }

    public void deleteAvatar(long userId) {
        User user = userQueryService.findById(userId);

        storageService.delete(user.getAvatar().getFilename());
        user.setAvatar(storageService.get("default-user-avatar_nsfvaz"));

        userQueryService.save(user);
    }

}
