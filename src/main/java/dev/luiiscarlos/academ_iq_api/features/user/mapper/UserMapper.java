package dev.luiiscarlos.academ_iq_api.features.user.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.user.dto.*;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;

@Component
public class UserMapper {

    public User toModel(RegisterRequest dto) {
        String fullname = String.format("%s %s", dto.getFirstname(), dto.getLastname());

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

    public User toModel(UpdateRequest dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .birthdate(dto.getBirthdate())
                .phone(dto.getPhone())
                .dni(dto.getDni())
                .githubUrl(dto.getGithubUrl())
                .linkedinUrl(dto.getLinkedinUrl())
                .websiteUrl(dto.getWebsiteUrl())
                .biography(dto.getBiography())
                .studies(dto.getStudies())
                .jobArea(dto.getJobArea())
                .workExperience(dto.getWorkExperience())
                .companyName(dto.getCompanyName())
                .manager(dto.isManager())
                .wantToUpgrade(dto.wantToUpgrade())
                .build();
    }

    public RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .birthdate(user.getBirthdate())
                .build();
    }

    public LoginResponse toLoginResponse(User user, String accessToken, String refreshToken) {
        UserResponse userResponse = this.toUserResponse(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    public UserResponse toUserResponse(User user) {
        String fullname = String.format("%s %s", user.getFirstname(), user.getLastname());

        return UserResponse.builder()
                .username(user.getUsername())
                .avatar(user.getAvatar().getUrl())
                .email(user.getEmail())
                .fullname(fullname)
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .birthdate(user.getBirthdate())
                .dni(user.getDni())
                .githubUrl(user.getGithubUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .websiteUrl(user.getWebsiteUrl())
                .jobArea(user.getJobArea())
                .workExperience(user.getWorkExperience())
                .studies(user.getStudies())
                .companyName(user.getCompanyName())
                .biography(user.getBiography())
                .hours(user.getHours())
                .wantToUpgrade(user.wantToUpgrade())
                .manager(user.isManager())
                .registeredAt(user.getRegisteredAt())
                .build();
    }

}
