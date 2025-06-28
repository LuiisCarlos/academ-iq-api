package dev.luiiscarlos.academ_iq_api.features.identity.user.mapper;

import org.springframework.stereotype.Component;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.*;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.identity.user.model.UserInfo;

@Component
public class UserMapper {

    public User toEntity(UserUpdateRequest dto) {
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

    public UserResponse toDto(User entity) {
        String fullname = entity.getFirstname() + " " + entity.getLastname();

        return UserResponse.builder()
                .username(entity.getUsername())
                .avatar(entity.getAvatar().getUrl())
                .email(entity.getEmail())
                .fullname(fullname)
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .phone(entity.getPhone())
                .birthdate(entity.getBirthdate())
                .dni(entity.getInfo().getDni())
                .githubUrl(entity.getInfo().getGithubUrl())
                .linkedinUrl(entity.getInfo().getLinkedinUrl())
                .websiteUrl(entity.getInfo().getWebsiteUrl())
                .jobArea(entity.getInfo().getJobArea())
                .workExperience(entity.getInfo().getWorkExperience())
                .studies(entity.getInfo().getStudies())
                .companyName(entity.getInfo().getCompanyName())
                .biography(entity.getInfo().getBiography())
                .hours(entity.getInfo().getHours())
                .manager(entity.getInfo().isManager())
                .registeredAt(entity.getRegisteredAt())
                .build();
    }

    public UserInstructorResponse toInstructorDto(User entity) {
		return UserInstructorResponse.builder()
				.fullname(entity.getFullname())
				.avatar(entity.getAvatar().getUrl())
				.build();
    }

}
