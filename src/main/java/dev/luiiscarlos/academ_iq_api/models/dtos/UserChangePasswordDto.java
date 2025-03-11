package dev.luiiscarlos.academ_iq_api.models.dtos;

import lombok.Data;

@Data
public class UserChangePasswordDto {

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

}
