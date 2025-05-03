package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import lombok.Data;

@Data
public class PasswordUpdateDto {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
