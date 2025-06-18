package dev.luiiscarlos.academ_iq_api.features.identity.auth.dto;

import dev.luiiscarlos.academ_iq_api.shared.validation.password.Password;
import dev.luiiscarlos.academ_iq_api.shared.validation.password.PasswordConfirmable;
import dev.luiiscarlos.academ_iq_api.shared.validation.password.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class ResetPasswordRequest implements PasswordConfirmable {

    @Password
    private String password;

    private String confirmPassword;

}
