package dev.luiiscarlos.academ_iq_api.dtos;

import lombok.Data;

@Data
public class UserRegisterRequestDto {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String firstname;

    private String lastname;

    private String phone;

}
