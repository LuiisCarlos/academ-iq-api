package dev.luiiscarlos.academ_iq_api.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String username;

    private String avatar;

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

    private Integer hours;

    private Boolean manager;

    private Boolean wantToUpgrade;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime registeredAt;

}
