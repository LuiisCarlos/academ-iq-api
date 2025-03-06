package dev.luiiscarlos.academ_iq_api.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.exceptions.BadCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.RoleNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserIsAlreadyRegisteredException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserRegistrationWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RoleRepository;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto login(UserLoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String accessToken = tokenService.generateAccessToken(user.getUsername(), user.getRoles());
        String refreshToken = tokenService.generateRefreshToken(user.getUsername(), user.getRoles());

        return userMapper.mapToUserLoginResponseDto(user, accessToken, refreshToken);
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword()))
            throw new UserRegistrationWithDifferentPasswordsException("Passwords do not match");

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent())
            throw new UserIsAlreadyRegisteredException("The user is not valid");

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        Role userRole = roleRepository.findByAuthority("USER")
            .orElseThrow(() -> new RoleNotFoundException("Role 'USER' not found"));
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        User user = userMapper.mapToUser(registerRequest, encodedPassword, authorities);

        return userMapper.mapToUserRegisterResponseDto(userRepository.save(user));
    }

    public String refreshToken(String refreshToken) {
        if (refreshToken == null)
            throw new AuthenticationCredentialsNotFoundException("Refresh token is required");

        if (tokenService.validateRefreshToken(refreshToken))
            throw new AuthenticationCredentialsNotFoundException("Refresh token is invalid");

        refreshToken = tokenService.getTokenFromRequest(refreshToken);

        String username = tokenService.getUsernameFromToken(refreshToken);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        return tokenService.generateAccessToken(user.getUsername(), user.getRoles());
    }

}
