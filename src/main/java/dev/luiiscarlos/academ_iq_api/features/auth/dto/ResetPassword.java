package dev.luiiscarlos.academ_iq_api.features.auth.dto;

import dev.luiiscarlos.academ_iq_api.core.validation.PasswordMatches;
import lombok.Data;

@Data
@PasswordMatches
public class ResetPassword {

    private String password;

    private String confirmPassword;

}
