package dev.luiiscarlos.academ_iq_api.mappers;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.dtos.LoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.RegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.RegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;

@Component
public class UserMapper {

    private static final String PASSWORD_PREFIX = "{bcrypt}";


    public RegisterResponseDto mapToRegisterResponseDto(User user) {
        return RegisterResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .build();
    }

    public RegisterResponseDto mapToRegisterResponseDto(User user, String accessToken, String refreshToken) {
        return RegisterResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .build();
    }

    public User mapToUser(RegisterRequestDto registerRequest, String encodedPassword, Set<Role> authorities) {
        return User.builder()
            .username(registerRequest.getUsername())
            .password(PASSWORD_PREFIX + encodedPassword)
            .roles(authorities)
            .email(registerRequest.getEmail())
            .firstname(registerRequest.getFirstname())
            .lastname(registerRequest.getLastname())
            .phone(registerRequest.getPhone())
            .build();
    }

    public LoginResponseDto mapToLoginResponseDto(User user, String accessToken, String refreshToken) {
        return LoginResponseDto.builder()
            .username(user.getUsername())
            .avatar(user.getAvatar())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public LoginResponseDto mapToLoginResponseDto(Authentication auth, String accessToken, String refreshToken) {
        User user = (User) auth.getPrincipal();

        return LoginResponseDto.builder()
            .username(user.getUsername())
            .avatar(user.getAvatar())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

}
