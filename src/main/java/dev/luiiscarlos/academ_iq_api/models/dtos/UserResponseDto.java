package dev.luiiscarlos.academ_iq_api.models.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private String username;

    private String avatarUrl;

    private String email;

    private String firstname;

    private String lastname;

    private String phone;

    private String dni;

    private String githubUrl;

    private String linkedinUrl;

    private String websiteUrl;

    private String biography;

    private String studies;

    private String jobArea;

    private String workExperience;

    private String companyName;

    private Boolean isTeamManager;

    private Boolean wantToUpgrade;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

}
