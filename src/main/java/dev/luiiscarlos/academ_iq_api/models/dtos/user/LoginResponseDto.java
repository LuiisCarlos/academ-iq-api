package dev.luiiscarlos.academ_iq_api.models.dtos.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

    private UserResponseDto user;

    private String accessToken;

    private String refreshToken;

}
