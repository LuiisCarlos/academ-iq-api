package dev.luiiscarlos.academ_iq_api.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

    private String username;

    private String avatar;

    private String email;

    private String firstname;

    private String lastname;

    private String accessToken;

    private String refreshToken;

}
