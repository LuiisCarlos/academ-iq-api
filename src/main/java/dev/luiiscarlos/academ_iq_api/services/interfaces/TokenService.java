package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.time.Instant;

import org.springframework.security.oauth2.jwt.Jwt;

import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.User;

public interface TokenService {

    RefreshToken findByToken(String token);

    String generateAccessToken(User user);

    RefreshToken generateRefreshToken(User user);

    String generateVerificationToken(User user);

    String generateRecoverPasswordToken(User user);

    String refreshAccessToken(String token);

    boolean isValidToken(String token);

    void invalidateRefreshToken(String token);

    String extractTokenFromJson(String tokenJson);

    Instant getTokenExpiration(String token);

    String getTokenType(String token);

    Jwt getJwtToken(String token);

}
