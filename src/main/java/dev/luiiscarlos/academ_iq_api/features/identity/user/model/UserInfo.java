package dev.luiiscarlos.academ_iq_api.features.identity.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String dni;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    private String biography;

    private String studies;

    @Column(name = "job_area")
    private String jobArea;

    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "company_name")
    private String companyName;

    @Builder.Default
    private Integer hours = 0;

    @Column(name = "is_manager")
    private Boolean manager;

    public Boolean isManager() {
        return this.manager;
    }

}
