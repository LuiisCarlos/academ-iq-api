package dev.luiiscarlos.academ_iq_api.features.identity.user.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.AdminPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserPasswordUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserUpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl.UserDetailsServiceImpl;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserAdminService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserCrudService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.dto.*;
import dev.luiiscarlos.academ_iq_api.features.storage.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.shared.security.AdminContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserDetailsServiceImpl userDetailsService;

    private final UserAdminService userAdminService;

    private final UserService userService;

    private final UserCrudService userCrudService;

    private final AdminContext adminContext;

    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (adminContext.isAdmin())
            log.info("Admin '{}' loaded user by username: '{}'", adminContext.admin(), username);

        return userDetails;
    }

    public Page<UserResponse> getAll(Pageable pageable) {
        Page<UserResponse> users = userAdminService.getAll(pageable);

        if (adminContext.isAdmin())
            log.info("Admin '{}' retrieved all users with pageable: {}", adminContext.admin(), pageable);

        return users;
    }

    public void assignRole(long userId, RoleRequest request) {
        userAdminService.assignRole(userId, request);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' assigned role '{}' to user with ID {}", adminContext.admin(), request, userId);
    }

    public void removeRole(long userId, RoleRequest request) {
        userAdminService.removeRole(userId, request);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' removed role '{}' from user with ID {}", adminContext.admin(), request, userId);
    }

    public void setRoles(long userId, RolesRequest request) {
        userAdminService.setRoles(userId, request);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' setted roles {} for user with ID {}", adminContext.admin(), request, userId);
    }

    public void changePassword(long userId, AdminPasswordRequest request) {
        userAdminService.changePassword(userId, request);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' changed password for user with ID {}", adminContext.admin(), userId);
    }

    public void forceLogout(long userId) {
        userAdminService.forceLogout(userId);

        if (adminContext.isAdmin())
            log.info("Admin '{}' forced logout for user with ID {}", adminContext.admin(), userId);
    }

    public void activate(long userId) {
        userAdminService.activate(userId);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' activated user with ID {}", adminContext.admin(), userId);
    }

    public void deactivate(long userId) {
        userAdminService.deactivate(userId);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' deactivated user with ID {}", adminContext.admin(), userId);
    }

    public UserResponse get(long userId) {
        UserResponse userResponse = userService.get(userId);

        if (adminContext.isAdmin())
            log.info("Admin '{}' retrieved user with ID {}", adminContext.admin(), userId);

        return userResponse;
    }

    public FileResponse getAvatar(long userId) {
        FileResponse fileResponse = userService.getAvatar(userId);

        if (adminContext.isAdmin())
            log.info("Admin '{}' retrieved avatar for user with ID {}", adminContext.admin(), userId);

        return fileResponse;
    }

    public UserResponse update(long userId, UserUpdateRequest request) {
        UserResponse userResponse = userService.update(userId, request);

        if (adminContext.isAdmin())
            log.info("Admin '{}' updated user with ID {}", adminContext.admin(), userId);

        return userResponse;
    }

    public FileResponse patchAvatar(long userId, MultipartFile multipartFile) {
        FileResponse fileResponse = userService.patchAvatar(userId, multipartFile);

        if (adminContext.isAdmin())
            log.info("Admin '{}' patched avatar for user with ID {}", adminContext.admin(), userId);

        return fileResponse;
    }

    public void updatePassword(long userId, UserPasswordUpdateRequest request) {
        userService.updatePassword(userId, request);

        if (adminContext.isAdmin())
            log.info("Admin '{}' updated password for user with ID {}", adminContext.admin(), userId);
    }

    public void delete(long userId) {
        userService.delete(userId);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' deleted user with ID {}", adminContext.admin(), userId);
    }

    public void deleteAvatar(long userId) {
        userService.deleteAvatar(userId);

        if (adminContext.isAdmin())
            log.warn("Admin '{}' deleted avatar for user with ID {}", adminContext.admin(), userId);
    }

    public User save(User user) {
        return userCrudService.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userCrudService.findAll(pageable);
    }

    public User findById(long id) {
        return userCrudService.findById(id);
    }

    public User findByUsername(String username) {
        return userCrudService.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userCrudService.findByEmail(email);
    }

    public User findReferenceById(long id) {
        return userCrudService.findReferenceById(id);
    }

    public User update(User user) {
        return userCrudService.update(user);
    }

    public void delete(User user) {
        userCrudService.delete(user);
    }

    public void deleteById(long id) {
        userCrudService.deleteById(id);
    }

    public boolean existsById(long id) {
        return userCrudService.existsById(id);
    }

    public boolean existsByUsername(String username) {
        return userCrudService.existsByUsername(username);
    }

}
