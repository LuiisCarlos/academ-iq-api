package dev.luiiscarlos.academ_iq_api.auth.dto;

import dev.luiiscarlos.academ_iq_api.shared.validation.PasswordMatches;
import lombok.Data;

@Data
@PasswordMatches
public class ResetPassword {

    private String password;

    private String confirmPassword;

}
