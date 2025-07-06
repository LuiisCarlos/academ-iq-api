package dev.luiiscarlos.academ_iq_api.features.identity.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

public interface UserCrudService {

    /**
     * Persists a new user or updates an existing user in the database
     *
     * @param user The user entity to save
     * @return The saved user entity
     */
    User save(User user);

    /**
     * Retrieves a paginated list of all users.
     *
     * @param pageable Pagination information
     * @return A page of users
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Finds a user by their unique ID
     *
     * @param id The user's ID
     * @return The user entity, or null if not found
     */
    User findById(long id);

    /**
     * Finds a user by their username
     *
     * @param username The user's username
     * @return The user entity, or null if not found
     */
    User findByUsername(String username);

    /**
     * Finds a user by their email address
     *
     * @param email The user's email
     * @return The user entity, or null if not found
     */
    User findByEmail(String email);

    /**
     * Gets a reference to a user by ID without fully loading the entity
     *
     * @param id The user's ID
     * @return A reference to the user entity
     */
    User findReferenceById(long id);

    /**
     * Updates an existing user in the database
     *
     * @param user The user entity with updated data
     * @return The updated user entity
     */
    User update(User user);

    /**
     * Deletes a user from the database
     *
     * @param user The user entity to delete
     */
    void delete(User user);

    /**
     * Deletes a user by their unique ID
     *
     * @param id The user's ID
     */
    void deleteById(long id);

    /**
     * Checks if a user exists by their unique ID
     *
     * @param id The user's ID
     * @return True if the user exists, false otherwise
     */
    boolean existsById(long id);

    /**
     * Checks if a user exists by their username
     *
     * @param username The user's username
     * @return True if the user exists, false otherwise
     */
    boolean existsByUsername(String username);

}
