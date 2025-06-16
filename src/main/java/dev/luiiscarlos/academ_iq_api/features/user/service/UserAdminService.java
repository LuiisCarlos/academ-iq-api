package dev.luiiscarlos.academ_iq_api.features.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.features.user.dto.UserResponse;

public interface UserAdminService {

    /**
     * Retrieves a paginated list of users
     *
     * @param pageable pagination and sorting information
     * @return a page of user responses
     */
    Page<UserResponse> getAll(Pageable pageable);

    /**
     * Assigns a specific role to a user
     *
     * @param userId the ID of the user
     * @param role   the role to assign
     */
    void assignRole(long userId, String role);

    /**
     * Removes a specific role from a user
     *
     * @param userId the ID of the user
     * @param role   the role to remove
     */
    void removeRole(long userId, String role);

    /**
     * Replaces the user's current roles with a new set
     *
     * @param userId the ID of the user
     * @param roles  the list of roles to set
     */
    void setRoles(long userId, List<String> roles);

    /**
     * Changes the user's password to the specified new one
     *
     * @param userId      the ID of the user
     * @param newPassword the new password to set
     */
    void changePassword(long userId, String newPassword);

    /**
     * Forces the user to log out from all active sessions
     *
     * @param userId the ID of the user
     */
    void forceLogout(long userId);

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
