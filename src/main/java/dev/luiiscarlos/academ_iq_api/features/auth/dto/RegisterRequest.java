package dev.luiiscarlos.academ_iq_api.features.auth.dto;

import java.time.LocalDate;

import dev.luiiscarlos.academ_iq_api.core.validation.adult.Adult;

import dev.luiiscarlos.academ_iq_api.core.validation.password.PasswordConfirmable;
import dev.luiiscarlos.academ_iq_api.core.validation.password.PasswordMatches;

import io.micrometer.common.lang.Nullable;

import lombok.Data;

@Data
@PasswordMatches
public class RegisterRequest implements PasswordConfirmable {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String firstname;

    private String lastname;

    @Nullable
    private String phone;

    @Adult
    private LocalDate birthdate;

}
