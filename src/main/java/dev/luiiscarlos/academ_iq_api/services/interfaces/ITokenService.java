package dev.luiiscarlos.academ_iq_api.services.interfaces;

import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.User;

public interface ITokenService {

    RefreshToken findByToken(String token);

    String generateAccessToken(User user);

    RefreshToken generateRefreshToken(User user);

    String refreshAccessToken(String token);

    boolean validateRefreshToken(String token);

    void invalidateRefreshToken(String token);

    String extractTokenFromJson(String tokenJson);

}
