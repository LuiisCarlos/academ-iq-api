package dev.luiiscarlos.academ_iq_api.features.auth.dto;

import dev.luiiscarlos.academ_iq_api.features.user.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private UserResponse user;

    private String accessToken;

    private String refreshToken;

}
