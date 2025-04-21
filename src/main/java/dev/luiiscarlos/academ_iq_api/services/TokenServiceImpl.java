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

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    /**
     * Generates an access token for the given user.
     *
     * @param user The user for whom the access token is generated.
     *
     * @return The generated access token as a String.
     */
    @Override
    public String generateAccessToken(User user) {
        Instant expiresAt = Instant.now().plus(1, ChronoUnit.HOURS);
        String tokenType = "access";

        return generateToken(user, expiresAt, tokenType);
    }

    /**
     * Generates a refresh token for the given user.
     * If a refresh token already exists for the user, it updates the existing one.
     *
     * @param user The user for whom the refresh token is generated.
     */
    @Override
    public RefreshToken generateRefreshToken(User user) {
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        ZoneId zoneId = ZoneId.of("Europe/Madrid");

        String tokenType = "refresh";
        String token = generateToken(user, expiresAt, tokenType);

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

    /**
     * Generates a verification token for the given user.
     * This token is used for email verification.
     *
     * @param user The user for whom the verification token is generated.
     *
     * @return The generated verification token as a String.
     */
    @Override
    public String generateVerificationToken(User user) {
        Instant expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);
        String tokenType = "verify";

        return generateToken(user, expiresAt, tokenType);
    }

    /**
     * Generates a password recovery token for the given user.
     * This token is used for password recovery.
     *
     * @param user The user for whom the password recovery token is generated.
     *
     * @return The generated password recovery token as a String.
     */
    @Override
    public String generateRecoverPasswordToken(User user) {
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
        String tokenType = "recover";

        return generateToken(user, expiresAt, tokenType);
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param token The refresh token used to generate a new access token.
     *
     * @return The new access token as a String.
     *
     * @throws RefreshTokenNotFoundException If the refresh token is not found in the database.
     * @throws InvalidCredentialsException If the refresh token is invalid or not found.
     * @throws RefreshTokenExpiredException If the refresh token has expired.
     */
    @Override
    public String refreshAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RefreshTokenNotFoundException(
                "Failed to refresh access token: Refresh token not found"));

        if (!isValidToken(token))
            throw new InvalidCredentialsException(
                "Failed to refresh access token: Invalid refresh token");

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RefreshTokenExpiredException(
                "Failed to refresh access token: Expired refresh token");

        return generateAccessToken(refreshToken.getUser());
    }

    /**
     * Validates the provided token.
     *
     * @param token The token to be validated.
     *
     * @return true if the token is valid, false otherwise.
     */
    @Override
    public boolean isValidToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Invalidates the provided refresh token.
     *
     * @param token The refresh token to be invalidated.
     */
    @Override
    public void invalidateRefreshToken(String token) {
        if (refreshTokenRepository.existsByToken(token))
            refreshTokenRepository.deleteByToken(token);
    }

    /**
     * Finds a refresh token by its value.
     *
     * @param token The value of the refresh token to be found.
     *
     * @return The found RefreshToken object.
     *
     * @throws RefreshTokenNotFoundException If the refresh token is not found in the database.
     */
    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new RefreshTokenNotFoundException(
                "Failed to find refresh token: Refresh token not found"));
    }

    /**
     * Finds a refresh token by its ID.
     *
     * @param id The id of the refresh token to be found.
     *
     * @return The found RefreshToken object.
     *
     * @throws RefreshTokenNotFoundException If the refresh token is not found in the database.
     */
    @Override
    public String getTokenType(String token) {
        try {
            return jwtDecoder.decode(token).getClaimAsString("token_type");
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    /**
     * Gets the expiration time of the provided token.
     *
     * @param token The token whose expiration time is to be retrieved.
     *
     * @return The expiration time of the token as an Instant.
     *
     * @throws InvalidTokenException If the token is malformed or invalid.
     */
    @Override
    public Instant getTokenExpiration(String token) {
        try {
            return jwtDecoder.decode(token).getExpiresAt();
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    /**
     * Gets the subject of the provided token.
     *
     * @param token The token whose subject is to be retrieved.
     *
     * @return The subject of the token as a String.
     *
     * @throws InvalidTokenException If the token is malformed or invalid.
     */
    public String getTokenSubject(String token) {
        try {
            return jwtDecoder.decode(token).getSubject();
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    /**
     * Gets the Jwt object from the provided token.
     *
     * @param token The token to be decoded.
     *
     * @return The decoded Jwt object.
     *
     * @throws InvalidTokenException If the token is malformed or invalid.
     */
    @Override
    public Jwt getJwtToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException ex){
            throw new InvalidTokenException("Failed to validate token: Malformed Token");
        }
    }

    /**
     * Generates a token for the given user with the specified expiration time and token type.
     * This method is used to create access, refresh, verification, and password recovery tokens.
     *
     * @param user The user for whom the token is generated.
     * @param expiresAt The expiration time of the token as an Instant.
     * @param tokenType The type of the token (e.g., "access", "refresh", "verify", "recover").
     *
     * @return The generated token as a String.
     */
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

}
