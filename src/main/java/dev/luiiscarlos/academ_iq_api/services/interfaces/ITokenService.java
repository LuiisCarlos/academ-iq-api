package dev.luiiscarlos.academ_iq_api.services.interfaces;

import dev.luiiscarlos.academ_iq_api.models.User;

public interface ITokenService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String refreshAccessToken(String token);

    boolean validateRefreshToken(String token);

    void invalidateRefreshToken(String token);

    String extractTokenFromJson(String tokenJson);

}
