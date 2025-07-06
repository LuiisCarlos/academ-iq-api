package dev.luiiscarlos.academ_iq_api.features.identity.user.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.shared.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.identity.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.identity.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.repository.UserRepository;
import dev.luiiscarlos.academ_iq_api.features.identity.user.service.UserCrudService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCrudServiceImpl implements UserCrudService {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        if (user == null)
            throw new UserNotFoundException("User is null");
        if (userRepository.existsById(user.getId()))
            throw new UserAlreadyExistsException("User already exists");

        return userRepository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        if (users.isEmpty())
            throw new UserNotFoundException("Users not found");

        return users;
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND, id)));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND_BY_NAME, username)));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ErrorMessages.USER_NOT_FOUND_BY_EMAIL, email)));
    }

    @Override
    public User findReferenceById(long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    public User update(User user) {
        if (!userRepository.existsById(user.getId()))
            throw new UserNotFoundException(
                    String.format(ErrorMessages.USER_NOT_FOUND, user.getId()));

        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.findById(user.getId()).ifPresentOrElse((u) -> {
            userRepository.deleteById(u.getId());
        }, () -> {
            throw new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, user.getId()));
        });
    }

    @Override
    public void deleteById(long id) {
        userRepository.findById(id).ifPresentOrElse((u) -> {
            userRepository.deleteById(u.getId());
        }, () -> {
            throw new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, id));
        });
    }

    @Override
    public boolean existsById(long id) {
        return userRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}
