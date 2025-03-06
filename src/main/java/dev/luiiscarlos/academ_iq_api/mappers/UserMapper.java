package dev.luiiscarlos.academ_iq_api.mappers;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;

@Component
public class UserMapper {

    private static String PASSWORD_PREFIX = "{bcrypt}";

    public UserRegisterResponseDto mapToUserRegisterResponseDto(User user) {
        return UserRegisterResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .build();
    }

    public UserRegisterResponseDto mapToUserRegisterResponseDto(User user, String accessToken, String refreshToken) {
        return UserRegisterResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .build();
    }

    public User mapToUser(UserRegisterRequestDto registerRequest, String encodedPassword, Set<Role> authorities) {
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

    public UserLoginResponseDto mapToUserLoginResponseDto(User user, String accessToken, String refreshToken) {
        return UserLoginResponseDto.builder()
            .username(user.getUsername())
            .avatar(user.getAvatar())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public UserLoginResponseDto mapToUserLoginResponseDto(Authentication auth, String accessToken, String refreshToken) {
        User user = (User) auth.getPrincipal();

        return UserLoginResponseDto.builder()
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
