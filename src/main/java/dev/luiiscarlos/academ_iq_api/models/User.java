package dev.luiiscarlos.academ_iq_api.models;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

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
    @Nullable
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String username;

    @Nullable
    private String password;

    @Nullable
    @JoinTable(
        name = "user_role_junction",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Role> authorities;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "file_id")
    private File avatar;

    @NonNull
    private String email;

    @Nullable
    private String firstname;

    @NonNull
    private String lastname;

    @NonNull
    private LocalDate birthdate;

    @Nullable
    private String phone;

    @Nullable
    private String dni;

    @Nullable
    @Column(name = "github_url")
    private String githubUrl;

    @Nullable
    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Nullable
    @Column(name = "website_url")
    private String websiteUrl;

    @Nullable
    private String biography;

    @Nullable
    private String studies;

    @Nullable
    @Column(name = "job_area")
    private String jobArea;

    @Nullable
    @Column(name = "work_experience")
    private String workExperience;

    @Nullable
    @Column(name = "company_name")
    private String companyName;

    @Nullable
    @Builder.Default
    private Integer hours = 0;

    @Nullable
    @Column(name = "is_team_manager")
    private Boolean isTeamManager;

    @Nullable
    @Column(name = "want_to_upgrade")
    private Boolean wantToUpgrade;

    @Nullable
    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    @Nullable
    @Builder.Default
    @Column(name = "registered_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Nullable
    @Builder.Default
    @Column(name = "updated_at")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Nullable
    @Builder.Default
    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Boolean isVerified() {
        return this.isVerified;
    }

    public Boolean isTeamManager() {
        return this.isTeamManager;
    }

    public Boolean wantToUpgrade() {
        return this.wantToUpgrade;
    }

}
