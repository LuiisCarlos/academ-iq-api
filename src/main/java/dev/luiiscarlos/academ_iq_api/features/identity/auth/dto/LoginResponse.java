package dev.luiiscarlos.academ_iq_api.features.identity.auth.dto;

import dev.luiiscarlos.academ_iq_api.features.identity.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private UserResponse user;

    private String accessToken;

    private String refreshToken;

}
