package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequestDto {

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String birthdate;

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

    public Boolean isTeamManager() {
        return this.isTeamManager;
    }

    public Boolean wantToUpgrade() {
        return this.wantToUpgrade;
    }

}
