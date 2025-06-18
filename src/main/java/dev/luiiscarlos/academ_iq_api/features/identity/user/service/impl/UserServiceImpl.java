package dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.file.exception.FileStorageException;
import dev.luiiscarlos.academ_iq_api.features.file.exception.InvalidFileTypeException;
import dev.luiiscarlos.academ_iq_api.features.file.mapper.FileMapper;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.file.model.FileType;
import dev.luiiscarlos.academ_iq_api.features.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdatePasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.exception.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.features.identity.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.UserInfo;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserService;

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
