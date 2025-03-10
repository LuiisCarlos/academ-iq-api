package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import dev.luiiscarlos.academ_iq_api.controllers.StorageController;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageFileNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.IUserService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService, IUserService {

    private final UserRepository userRepository;

    private final StorageService storageService;

    private final TokenService tokenService;


    /**
     * Finds all users
     *
     * @return the list of users
     */
    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty())
            throw new UserNotFoundException("Users not found");

        return users;
    }

    /**
     * Finds the user by its id
     *
     * @param id the id of the user
     * @return the user
     */
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());
    }

    /**
     * Finds the user's avatar by its id
     *
     * @param id the id of the user
     * @return the user's avatar
     */
    public Resource findAvatarById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());

        String filePath = user.getAvatar();

        if (filePath == null)
            throw new StorageFileNotFoundException();

        String filename = StringUtils.getFilename(filePath);

        return storageService.loadAsResource(filename);
    }

    /**
     * Updates the user's information by its id
     *
     * @param id the id of the user
     * @param user the new user
     * @return the updated user
     */
    public User updateById(Long id, User user) {
        return userRepository.findById(id).map(u -> {
            u.setUsername(user.getUsername());
            u.setFirstname(user.getFirstname());
            u.setLastname(user.getLastname());
            u.setEmail(user.getEmail());
            u.setPhone(user.getPhone());
            u.setBirthdate(user.getBirthdate());
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException());
    }

    /**
     * Updates the user's avatar by its id
     *
     * @param id the id of the user
     * @param avatar the new avatar
     * @return the updated avatar
     */
    public Resource updateAvatarById(Long id, MultipartFile avatar) {
        if (avatar.isEmpty())
            throw new StorageException("File is required");

        String filename = storageService.store(avatar);
        String urlFile = MvcUriComponentsBuilder
            .fromMethodName(StorageController.class, "findByFilename", filename, null)
            .build()
            .toUriString();

        Resource newAvatar = userRepository.findById(id).map(u -> {
            if (u.getAvatar() != null)
                    storageService.delete(StringUtils.getFilename(u.getAvatar()));

            u.setAvatar(urlFile);
            userRepository.save(u);
            return storageService.loadAsResource(StringUtils.getFilename(u.getAvatar()));
        }).orElseThrow(() -> new UserNotFoundException());

        return newAvatar;
    }

    /**
     * Deletes the user by its id
     *
     * @param id the id of the user
     */
    public void deleteById(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException();

        userRepository.deleteById(id);
    }

    /**
     * Deletes the user's avatar by its id
     *
     * @param id the id of the user
     */
    public void deleteAvatarById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException());

        if (user.getAvatar() == null)
            throw new StorageFileNotFoundException();

        storageService.delete(StringUtils.getFilename(user.getAvatar()));
    }

    /**
     * Finds the user by the token
     *
     * @param token the token of the user
     * @return the user
     */
    public User findByToken(String token) {
        if (token == null)
            throw new InvalidCredentialsException("Token is required");

        token = token.substring(7);

        if (!tokenService.validateRefreshToken(token))
            throw new InvalidCredentialsException("Token is invalid");

        String username = tokenService.extractUsernameFromToken(token);
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException());
    }

    /**
     * Finds the user's avatar by the token
     *
     * @param token the token of the user
     * @return the user's avatar
     */
    public Resource findAvatarByToken(String token) {
        User user = this.findByToken(token);

        String filePath = user.getAvatar();

        if (filePath == null)
            throw new StorageFileNotFoundException();

        String filename = StringUtils.getFilename(filePath);

        return storageService.loadAsResource(filename);
    }

    /**
     * Updates the user's information by the token
     *
     * @param token the token of the user
     * @return the updated user
     */
    public User updateByToken(String token) {
        User user = this.findByToken(token);

        return updateById(user.getId(), user);
    }

    /**
     * Updates the user's avatar by the token
     *
     * @param token the token of the user
     * @param avatar the new avatar
     * @return the updated avatar
     */
    public Resource updateAvatarByToken(String token, MultipartFile avatar) {
        User user = this.findByToken(token);

        return updateAvatarById(user.getId(), avatar);
    }

    /**
     * Deletes the user by the token
     *
     * @param token the token of the user
     */
    public void deleteByToken(String token) {
        User user = this.findByToken(token);

        deleteById(user.getId());
    }

    /**
     * Deletes the user's avatar by the token
     *
     * @param token the token of the user
     */
    public void deleteAvatarByToken(String token) {
        User user = this.findByToken(token);

        deleteAvatarById(user.getId());
    }

    /**
     * Loads the user by the username
     *
     * @param username the username of the user
     * @return the user
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In the user details service");
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException());
    }

}
