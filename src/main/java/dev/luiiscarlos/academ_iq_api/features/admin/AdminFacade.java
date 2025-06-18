package dev.luiiscarlos.academ_iq_api.features.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdatePasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.facade.UserFacade;
import dev.luiiscarlos.academ_iq_api.features.identity.user.security.RoleType;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminFacade {

    private final UserFacade userFacade;

    public UserDetails loadUserByUsername(String username) {
        UserDetails userDetails = userFacade.loadUserByUsername(username);

        log.info("Admin '{}' loaded user by username: '{}'", admin(), username);

        return userDetails;
    }

    public Page<UserResponse> getAll(Pageable pageable) {
        Page<UserResponse> users = userFacade.getAll(pageable);

        log.info("Admin '{}' retrieved all users with pageable: {}", admin(), pageable);

        return users;
    }

    public void assignRole(long userId, RoleType role) {
        userFacade.assignRole(userId, role);

        log.warn("Admin '{}' assigned role '{}' to user with ID {}", admin(), role, userId);
    }

    public void removeRole(long userId, RoleType role) {
        userFacade.removeRole(userId, role);

        log.warn("Admin '{}' removed role '{}' from user with ID {}", admin(), role, userId);
    }

    public void setRoles(long userId, List<RoleType> roles) {
        userFacade.setRoles(userId, roles);

        log.warn("Admin '{}' set roles {} for user with ID {}", admin(), roles, userId);
    }

    public void changePassword(long userId, String newPassword) {
        userFacade.changePassword(userId, newPassword);

        log.warn("Admin '{}' changed password for user with ID {}", admin(), userId);
    }

    public void forceLogout(long userId) {
        userFacade.forceLogout(userId);

        log.info("Admin '{}' forced logout for user with ID {}", admin(), userId);
    }

    public void activate(long userId) {
        userFacade.activate(userId);

        log.warn("Admin '{}' activated user with ID {}", admin(), userId);
    }

    public void deactivate(long userId) {
        userFacade.deactivate(userId);

        log.warn("Admin '{}' deactivated user with ID {}", admin(), userId);
    }

    public UserResponse get(long userId) {
        UserResponse userResponse = userFacade.get(userId);

        log.info("Admin '{}' retrieved user with ID {}", admin(), userId);

        return userResponse;
    }

    public FileResponse getAvatar(long userId) {
        FileResponse fileResponse = userFacade.getAvatar(userId);

        log.info("Admin '{}' retrieved avatar for user with ID {}", admin(), userId);

        return fileResponse;
    }

    public UserResponse update(long userId, UpdateRequest request) {
        UserResponse userResponse = userFacade.update(userId, request);

        log.info("Admin '{}' updated user with ID {}", admin(), userId);

        return userResponse;
    }

    public FileResponse patchAvatar(long userId, MultipartFile multipartFile) {
        FileResponse fileResponse = userFacade.patchAvatar(userId, multipartFile);

        log.info("Admin '{}' patched avatar for user with ID {}", admin(), userId);

        return fileResponse;
    }

    public void updatePassword(long userId, UpdatePasswordRequest request) {
        userFacade.updatePassword(userId, request);

        log.info("Admin '{}' updated password for user with ID {}", admin(), userId);
    }

    public void delete(long userId) {
        userFacade.delete(userId);

        log.warn("Admin '{}' deleted user with ID {}", admin(), userId);
    }

    public void deleteAvatar(long userId) {
        userFacade.deleteAvatar(userId);

        log.warn("Admin '{}' deleted avatar for user with ID {}", admin(), userId);
    }

    private String admin() {
        Long userId = (long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userFacade.get(userId).getUsername();
    }

}
