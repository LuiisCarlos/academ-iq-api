package dev.luiiscarlos.academ_iq_api.models.mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserResponseDto;

@Component
public class UserMapper {

    public User toUser(UserRegisterRequestDto registerRequest, String encodedPassword, Set<Role> authorities) {
        return User.builder()
            .username(registerRequest.getUsername())
            .password(encodedPassword)
            .authorities(authorities)
            .email(registerRequest.getEmail())
            .firstname(registerRequest.getFirstname())
            .lastname(registerRequest.getLastname())
            .phone(registerRequest.getPhone())
            .birthdate(LocalDate.parse(registerRequest.getBirthdate()))
            .build();
    }

    public UserRegisterResponseDto toUserRegisterResponseDto(User user) {
        return UserRegisterResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .phone(user.getPhone())
            .birthdate(user.getBirthdate())
            .build();
    }

    public UserLoginResponseDto toUserLoginResponseDto(User user, String accessToken, String refreshToken, Instant refreshTokenExpiresAt) {
        return UserLoginResponseDto.builder()
            .username(user.getUsername())
            .avatarUrl(user.getAvatarUrl())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .phone(user.getPhone())
            .birthdate(user.getBirthdate())
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .refreshTokenExpiresAt(refreshTokenExpiresAt.toString())
            .build();
    }

    /* ------------------------------------------------------------------------------------------------------ */

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
            .username(user.getUsername())
            .avatarUrl(user.getAvatarUrl())
            .email(user.getEmail())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .phone(user.getPhone())
            .birthdate(user.getBirthdate())
            .build();
    }

}
