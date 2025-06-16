package dev.luiiscarlos.academ_iq_api.features.user.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                    String.format(ErrorMessages.USER_NOT_FOUND_BY_NAME, username)));
    }

}
