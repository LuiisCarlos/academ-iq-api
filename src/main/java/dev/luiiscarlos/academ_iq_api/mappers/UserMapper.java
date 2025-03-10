package dev.luiiscarlos.academ_iq_api.mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserResponseDto;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;

@Component
public class UserMapper {

    private static String PASSWORD_PREFIX = "{bcrypt}";

    public User mapToUser(UserRegisterRequestDto registerRequest, String encodedPassword, Set<Role> authorities) {
        return User.builder()
            .username(registerRequest.getUsername())
            .password(PASSWORD_PREFIX + encodedPassword)
            .authorities(authorities)
            .email(registerRequest.getEmail())
            .firstname(registerRequest.getFirstname())
            .lastname(registerRequest.getLastname())
            .phone(registerRequest.getPhone())
            .birthdate(registerRequest.getBirthdate())
            .registeredAt(LocalDateTime.now())
            .build();
    }

    public UserRegisterResponseDto mapToUserRegisterResponseDto(User user) {
        return UserRegisterResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .phone(user.getPhone())
            .birthdate(user.getBirthdate().toString())
            .build();
    }

    public UserLoginResponseDto mapToUserLoginResponseDto(User user, String accessToken, String refreshToken, Instant refreshTokenExpiresAt) {
        return UserLoginResponseDto.builder()
            .username(user.getUsername())
            .avatar(user.getAvatar())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .phone(user.getPhone())
            .birthdate(user.getBirthdate().toString())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .refreshTokenExpiresAt(refreshTokenExpiresAt.toString())
            .build();
    }

    /* ------------------------------------------------------------------------------------------------------ */

    public UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
            .username(user.getUsername())
            .avatar(user.getAvatar())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .phone(user.getPhone())
            .birthdate(user.getBirthdate().toString())
            .build();
    }

}
