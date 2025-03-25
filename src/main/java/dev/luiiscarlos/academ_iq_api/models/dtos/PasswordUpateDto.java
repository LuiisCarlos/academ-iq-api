package dev.luiiscarlos.academ_iq_api.models.dtos;

import lombok.Data;

@Data
public class PasswordUpateDto {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
