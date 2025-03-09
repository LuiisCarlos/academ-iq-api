package dev.luiiscarlos.academ_iq_api.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenExpiredException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.ITokenService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final ObjectMapper objectMapper;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private String generateToken(User user, Instant expiresAt, String tokenType) {
        Instant now = Instant.now();

        String scope = user.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.getUsername())
                .claim("roles", scope)
                .claim("token_type", tokenType)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateAccessToken(User user) {
        Instant expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES);
        String tokenType = "access";

        return generateToken(user, expiresAt, tokenType);
    }

    public String generateRefreshToken(User user) {
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        String tokenType = "refresh";

        String token = generateToken(user, expiresAt, tokenType);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).map(rt -> {
            rt.setToken(token);
            rt.setExpiresAt(expiresAt);
            return rt;
        }).orElse(RefreshToken.builder()
            .token(token)
            .user(user)
            .expiresAt(expiresAt)
            .createdAt(Instant.now())
            .build());

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public String refreshAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found"));

        if (!validateRefreshToken(token))
            throw new InvalidCredentialsException("Refresh token is invalid");

        if (refreshToken.getExpiresAt().isBefore(Instant.now()))
            throw new RefreshTokenExpiredException("Refresh token expired");

        return generateAccessToken(refreshToken.getUser());
    }

    public boolean validateRefreshToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractTokenFromJson(String TokenJson) {
        try {
            JsonNode rootNode = objectMapper.readTree(TokenJson);
            return rootNode.path("refreshToken").asText();
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void invalidateRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

}
