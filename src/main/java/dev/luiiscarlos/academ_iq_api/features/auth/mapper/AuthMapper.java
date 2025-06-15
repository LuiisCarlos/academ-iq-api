package dev.luiiscarlos.academ_iq_api.features.auth.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.user.dto.UserResponse;
import dev.luiiscarlos.academ_iq_api.features.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;

@Component
public class AuthMapper {

    private UserMapper userMapper;

    public User toModel(RegisterRequest dto) {
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

    public LoginResponse toLoginResponse(User model, String accessToken, String refreshToken) {
        UserResponse userResponse = userMapper.toUserResponse(model);

        return LoginResponse.builder()
                .user(userResponse)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RegisterResponse toRegisterResponse(User model) {
        String fullname = model.getFirstname() + " " + model.getLastname();

        return RegisterResponse.builder()
                .username(model.getUsername())
                .email(model.getEmail())
                .fullname(fullname)
                .firstname(model.getFirstname())
                .lastname(model.getLastname())
                .phone(model.getPhone())
                .birthdate(model.getBirthdate())
                .build();
    }

}
