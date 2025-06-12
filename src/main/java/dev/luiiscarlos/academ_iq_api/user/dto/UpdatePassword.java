package dev.luiiscarlos.academ_iq_api.user.dto;

import lombok.Data;

@Data
public class UpdatePassword {

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

}
