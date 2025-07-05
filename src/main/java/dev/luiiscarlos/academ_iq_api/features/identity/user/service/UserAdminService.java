package dev.luiiscarlos.academ_iq_api.features.identity.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.AdminPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.dto.RoleRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.dto.RolesRequest;

public interface UserAdminService {

    /**
     * Replaces the user's current roles with a new set
     *
     * @param userId the ID of the user
     * @param roles  the list of roles to set
     */
    void setRoles(long userId, RolesRequest request);

    /**
     * Assigns a specific role to a user
     *
     * @param userId the ID of the user
     * @param role   the role to assign
     */
    void assignRole(long userId, RoleRequest request);

    /**
     * Removes a specific role from a user
     *
     * @param userId the ID of the user
     * @param role   the role to remove
     */
    void removeRole(long userId, RoleRequest request);

    /**
     * Changes the user's password to the specified new one
     *
     * @param userId      the ID of the user
     * @param newPassword the new password to set
     */
    void changePassword(long userId, AdminPasswordRequest request);

    /**
     * Forces the user to log out from all active sessions
     *
     * @param userId the ID of the user
     */
    void forceLogout(long userId);

    /**
     * Retrieves a paginated list of users
     *
     * @param pageable pagination and sorting information
     * @return a page of user responses
     */
    Page<UserResponse> getAll(Pageable pageable);

    /**
     * Activates a user's account
     *
     * @param userId the ID of the user
     */
    void activate(long userId);

    /**
     * Deactivates a user's account
     *
     * @param userId the ID of the user
     */
    void deactivate(long userId);

}
