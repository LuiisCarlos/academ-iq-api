package dev.luiiscarlos.academ_iq_api.features.user.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public User save(User user) {
        if (user == null)
            throw new UserNotFoundException("User is null");
        if (userRepository.existsById(user.getId()))
            throw new UserAlreadyExistsException("User already exists");

        return userRepository.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty())
            throw new UserNotFoundException("Users not found");

        return users;
    }

    public User findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, userId)));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND_BY_NAME, username)));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND_BY_EMAIL, email)));
    }

    public User update(User user) {
        if (!userRepository.existsById(user.getId()))
            throw new UserNotFoundException(
                    String.format(ErrorMessages.USER_NOT_FOUND, user.getId()));

        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.findById(user.getId()).ifPresentOrElse((u) -> {
            userRepository.deleteById(u.getId());
        }, () -> {
            throw new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, user.getId()));
        });
    }

    public void deleteById(long userId) {
        userRepository.findById(userId).ifPresentOrElse((u) -> {
            userRepository.deleteById(u.getId());
        }, () -> {
            throw new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, userId));
        });
    }

    public boolean existsById(long userId) {
        return userRepository.existsById(userId);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
