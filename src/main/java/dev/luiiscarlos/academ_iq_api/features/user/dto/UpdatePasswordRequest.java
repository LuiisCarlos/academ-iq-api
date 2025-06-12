package dev.luiiscarlos.academ_iq_api.features.user.dto;

import dev.luiiscarlos.academ_iq_api.core.validation.password.PasswordConfirmable;
import dev.luiiscarlos.academ_iq_api.core.validation.password.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class UpdatePasswordRequest implements PasswordConfirmable {

    private String currentPassword;

    private String password;

    private String confirmPassword;

}
