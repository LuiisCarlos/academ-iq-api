package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import lombok.Data;

@Data
public class PasswordResetDto {

    private String password;

    private String confirmPassword;

}
