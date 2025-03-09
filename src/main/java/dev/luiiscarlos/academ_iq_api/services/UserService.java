package dev.luiiscarlos.academ_iq_api.services;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import dev.luiiscarlos.academ_iq_api.controllers.StorageController;
import dev.luiiscarlos.academ_iq_api.exceptions.StorageException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final StorageService storageService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In the user details service"); // ! output for debugging
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty())
            throw new UserNotFoundException("Users not found");

        return users;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User update(Long id, User user) {
        return userRepository.findById(id).map(u -> {
            u.setUsername(user.getUsername());
            u.setFirstname(user.getFirstname());
            u.setLastname(user.getLastname());
            u.setEmail(user.getEmail());
            u.setPhone(user.getPhone());
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User updateAvatar(Long id, @NonNull MultipartFile avatar) {
        if (avatar.isEmpty())
            throw new StorageException("Avatar is empty");

        String image = storageService.store(avatar);
        String urlImage = MvcUriComponentsBuilder // ! Modify this
            .fromMethodName(StorageController.class, "serve", image, null)
            .build()
            .toUriString();

        return userRepository.findById(id).map(u -> {
            u.setAvatar(urlImage);
            return userRepository.save(u);
        }).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException("User not found with id: " + id);

        userRepository.deleteById(id);
    }

}
