package dev.luiiscarlos.academ_iq_api.dtos;

import lombok.Data;

@Data
public class UserLoginRequestDto {

    private String username;

    private String password;

}
