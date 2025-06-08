package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import dev.luiiscarlos.academ_iq_api.utilities.annotations.PasswordMatches;

import lombok.Data;

@Data
@PasswordMatches
public class ResetPasswordDto {

    private String password;

    private String confirmPassword;

}
