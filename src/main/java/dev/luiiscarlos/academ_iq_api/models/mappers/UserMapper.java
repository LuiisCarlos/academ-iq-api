package dev.luiiscarlos.academ_iq_api.models.mappers;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.*;

@Component
public class UserMapper {

    public User toModel(UserRegisterRequestDto userDto) {
        String fullname = String.format("%s %s", userDto.getUsername(), userDto.getLastname());

        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .fullname(fullname)
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .phone(userDto.getPhone())
                .birthdate(userDto.getBirthdate())
                .build();
    }

    public User toModel(UserUpdateRequestDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .birthdate(LocalDate.parse(userDto.getBirthdate()))
                .phone(userDto.getPhone())
                .dni(userDto.getDni())
                .githubUrl(userDto.getGithubUrl())
                .linkedinUrl(userDto.getLinkedinUrl())
                .websiteUrl(userDto.getWebsiteUrl())
                .biography(userDto.getBiography())
                .studies(userDto.getStudies())
                .jobArea(userDto.getJobArea())
                .workExperience(userDto.getWorkExperience())
                .companyName(userDto.getCompanyName())
                .manager(userDto.isManager())
                .wantToUpgrade(userDto.wantToUpgrade())
                .build();
    }

    public UserRegisterResponseDto toRegisterResponseDto(User user) {
        return UserRegisterResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .birthdate(user.getBirthdate())
                .build();
    }

    public LoginResponseDto toLoginResponseDto(
            String accessToken,
            String refreshToken,
            User user) {
        UserResponseDto userResponse = this.toResponseDto(user);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    public UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .avatarUrl(user.getAvatar().getUrl())
                .email(user.getEmail())
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
