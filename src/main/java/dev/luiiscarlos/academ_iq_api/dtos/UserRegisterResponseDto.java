package dev.luiiscarlos.academ_iq_api.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponseDto {

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String phone;

    private String birthdate;

}
