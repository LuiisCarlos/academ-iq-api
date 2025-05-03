package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String username;

    private String password;

}
