package dev.luiiscarlos.academ_iq_api.features.auth.dto;

import dev.luiiscarlos.academ_iq_api.core.validation.password.PasswordConfirmable;
import dev.luiiscarlos.academ_iq_api.core.validation.password.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class ResetPasswordRequest implements PasswordConfirmable {

    private String password;

    private String confirmPassword;

}
