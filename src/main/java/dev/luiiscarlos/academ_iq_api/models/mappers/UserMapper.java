package dev.luiiscarlos.academ_iq_api.models.mappers;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.*;

@Component
public class UserMapper {

    public User toUser(
            UserRegisterRequestDto userDto,
            String encodedPassword,
            Set<Role> authorities,
            File avatar) {
        return User.builder()
            .username(userDto.getUsername())
            .password(encodedPassword)
            .authorities(authorities)
            .avatar(avatar)
            .email(userDto.getEmail())
            .firstname(userDto.getFirstname())
            .lastname(userDto.getLastname())
            .phone(userDto.getPhone())
            .birthdate(LocalDate.parse(userDto.getBirthdate()))
            .build();
    }

    public User toUser(UserUpdateRequestDto userDto) {
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
            .isTeamManager(userDto.isTeamManager())
            .wantToUpgrade(userDto.wantToUpgrade())
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

    public UserLoginResponseDto toUserLoginResponseDto(
            String accessToken,
            String refreshToken,
            User user) {
        UserResponseDto userResponse = this.toUserResponseDto(user);

        return UserLoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(userResponse)
            .build();
    }

    /* ------------------------------------------------------------------------------------------------------ */

    @SuppressWarnings("null")
    public UserResponseDto toUserResponseDto(User user) {
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
            .isTeamManager(user.isTeamManager())
            .registeredAt(user.getRegisteredAt())
            .build();
    }

}
