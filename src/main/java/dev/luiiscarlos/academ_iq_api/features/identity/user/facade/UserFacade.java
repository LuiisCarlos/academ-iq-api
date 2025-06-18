package dev.luiiscarlos.academ_iq_api.features.identity.user.facade;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dev.luiiscarlos.academ_iq_api.features.file.dto.FileResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdatePasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UpdateRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserAdminService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserService;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl.UserDetailsServiceImpl;
import dev.luiiscarlos.academ_iq_api.features.identity.user.security.RoleType;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final UserAdminService userAdminService;

    private final UserService userService;

    public UserDetails loadUserByUsername(String username) {
        return userDetailsServiceImpl.loadUserByUsername(username);
    }

    public Page<UserResponse> getAll(Pageable pageable) {
        return userAdminService.getAll(pageable);
    }

    public void assignRole(long userId, RoleType role) {
        userAdminService.assignRole(userId, role);
    }

    public void removeRole(long userId, RoleType role) {
        userAdminService.removeRole(userId, role);
    }

    public void setRoles(long userId, List<RoleType> roles) {
        userAdminService.setRoles(userId, roles);
    }

    public void changePassword(long userId, String newPassword) {
        userAdminService.changePassword(userId, newPassword);
    }

    public void forceLogout(long userId) {
        userAdminService.forceLogout(userId);
    }

    public void activate(long userId) {
        userAdminService.activate(userId);
    }

    public void deactivate(long userId) {
        userAdminService.deactivate(userId);
    }

    public UserResponse get(long userId) {
        return userService.get(userId);
    }

    public FileResponse getAvatar(long userId) {
        return userService.getAvatar(userId);
    }

    public UserResponse update(long userId, UpdateRequest request) {
        return userService.update(userId, request);
    }

    public FileResponse patchAvatar(long userId, MultipartFile MultipartFile){
        return userService.patchAvatar(userId, MultipartFile);
    }

    public void updatePassword(long userId, UpdatePasswordRequest request) {
        userService.updatePassword(userId, request);
    }

    public void delete(long userId) {
        userService.delete(userId);
    }

    public void deleteAvatar(long userId) {
        userService.deleteAvatar(userId);
    }

}
