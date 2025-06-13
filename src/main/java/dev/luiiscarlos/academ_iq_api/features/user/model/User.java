package dev.luiiscarlos.academ_iq_api.features.user.model;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.features.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.user.security.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    private File avatar;

    private String username;

    private String email;

    private String password;

    private String fullname;

    private String firstname;

    private String lastname;

    @JsonFormat(shape = Shape.STRING)
    private LocalDate birthdate;

    private String phone;

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

    @Column(name = "want_to_upgrade")
    private Boolean wantToUpgrade;

    @Builder.Default
    @Column(name = "is_verified")
    private Boolean verified = false;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    @JoinTable(name = "user_role_junction",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Role> authorities;

    @Builder.Default
    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "registered_at")
    @JsonFormat(shape = Shape.STRING)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Boolean isVerified() {
        return this.verified;
    }

    public Boolean isManager() {
        return this.manager;
    }

    public Boolean wantToUpgrade() {
        return this.wantToUpgrade;
    }

}
