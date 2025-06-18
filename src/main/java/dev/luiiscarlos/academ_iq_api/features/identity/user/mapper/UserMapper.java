package dev.luiiscarlos.academ_iq_api.features.identity.user.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.*;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.UserInfo;

@Component
public class UserMapper {

    public User toModel(UpdateRequest dto) {
        UserInfo userInfo = UserInfo.builder()
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
                .build();

        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .birthdate(dto.getBirthdate())
                .phone(dto.getPhone())
                .info(userInfo)
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
                .dni(user.getInfo().getDni())
                .githubUrl(user.getInfo().getGithubUrl())
                .linkedinUrl(user.getInfo().getLinkedinUrl())
                .websiteUrl(user.getInfo().getWebsiteUrl())
                .jobArea(user.getInfo().getJobArea())
                .workExperience(user.getInfo().getWorkExperience())
                .studies(user.getInfo().getStudies())
                .companyName(user.getInfo().getCompanyName())
                .biography(user.getInfo().getBiography())
                .hours(user.getInfo().getHours())
                .manager(user.getInfo().isManager())
                .registeredAt(user.getRegisteredAt())
                .build();
    }

}
