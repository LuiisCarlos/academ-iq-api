package dev.luiiscarlos.academ_iq_api.features.identity.auth.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.identity.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.identity.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.identity.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final UserMapper userMapper;

    public User toEntity(RegisterRequest dto) {
        String fullname = dto.getFirstname() + " " + dto.getLastname();

        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .fullname(fullname)
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .phone(dto.getPhone())
                .birthdate(dto.getBirthdate())
                .build();
    }

    public LoginResponse toDto(User entity, String accessToken, String refreshToken) {
        UserResponse userResponse = userMapper.toDto(entity);

        return LoginResponse.builder()
                .user(userResponse)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Converts a User model to a RegisterResponse DTO
     *
     * @param entity {@link User} the User model to convert
     * @return a {@link RegisterResponse} containing user information
     */
    public RegisterResponse toDto(User entity) {
        String fullname = entity.getFirstname() + " " + entity.getLastname();

        return RegisterResponse.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .fullname(fullname)
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .phone(entity.getPhone())
                .birthdate(entity.getBirthdate())
                .build();
    }

}
