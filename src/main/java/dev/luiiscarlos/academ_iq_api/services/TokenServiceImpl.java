package dev.luiiscarlos.academ_iq_api.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenExpiredException;
import dev.luiiscarlos.academ_iq_api.exceptions.RefreshTokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RefreshTokenRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.TokenService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final String TOKEN_PREFIX = "Bearer ";

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private String generateToken(User user, Instant expiresAt, String tokenType) {
        Instant now = Instant.now();

        String scope = user.getAuthorities().stream()
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
        Instant expiresAt = Instant.now().plus(1, ChronoUnit.HOURS);
        String tokenType = "access";

        return TOKEN_PREFIX + generateToken(user, expiresAt, tokenType);
    }

    public RefreshToken generateRefreshToken(User user) {
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.of("Europe/Madrid");

        String tokenType = "refresh";
        String token = TOKEN_PREFIX + generateToken(user, expiresAt, tokenType);

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).map(rt -> {
            rt.setToken(token);
            rt.setExpiresAt(LocalDateTime.ofInstant(expiresAt, zoneId));
            return rt;
        }).orElse(RefreshToken.builder()
            .token(token)
            .user(user)
            .expiresAt(LocalDateTime.ofInstant(expiresAt, zoneId))
            .build());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public String generateVerificationToken(User user) {
        Instant expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);
        String tokenType = "verify";

        return generateToken(user, expiresAt, tokenType);
    }

    @Override
    public String generateRecoverPasswordToken(User user) {
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
        String tokenType = "recover";

        return generateToken(user, expiresAt, tokenType);
    }

    public String refreshAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(TOKEN_PREFIX + token)
            .orElseThrow(() -> new RefreshTokenNotFoundException("Failed to refresh access token: Refresh token not found"));

        if (!isValidToken(token))
            throw new InvalidCredentialsException("Failed to refresh access token: Invalid refresh token");

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RefreshTokenExpiredException("Failed to refresh access token: Expired refresh token");

        return generateAccessToken(refreshToken.getUser());
    }

    public boolean isValidToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public void invalidateRefreshToken(String token) {
        if (refreshTokenRepository.existsByToken(token))
            refreshTokenRepository.deleteByToken(token);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RefreshTokenNotFoundException("Failed to find refresh token: Refresh token not found"));
    }

    public String getTokenType(String token) {
        try {
            return jwtDecoder.decode(token).getClaimAsString("token_type");
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    public Instant getTokenExpiration(String token) {
        try {
            return jwtDecoder.decode(token).getExpiresAt();
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    public String getTokenSubject(String token) {
        try {
            return jwtDecoder.decode(token).getSubject();
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    public Jwt getJwtToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }
}
