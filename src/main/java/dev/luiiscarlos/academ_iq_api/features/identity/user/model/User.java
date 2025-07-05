package dev.luiiscarlos.academ_iq_api.features.identity.user.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import dev.luiiscarlos.academ_iq_api.domain.billing.subcription.SubscriptionPlan;
import dev.luiiscarlos.academ_iq_api.domain.billing.subcription.SubscriptionStatus;
import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.model.Role;
import dev.luiiscarlos.academ_iq_api.features.learning.enrollment.model.Enrollment;
import dev.luiiscarlos.academ_iq_api.features.storage.model.File;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
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

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status")
    private SubscriptionStatus subscriptionStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan")
    private SubscriptionPlan subscriptionPlan;

    @Embedded
    private UserInfo info;

    @Builder.Default
    @Column(name = "is_verified")
    private Boolean verified = false;

    @Builder.Default
    @Column(name = "is_enabled")
    private Boolean enabled = Boolean.TRUE;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role_junction",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
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
        return this.authorities;
    }

    public Boolean isVerified() {
        return this.verified;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
