package dev.luiiscarlos.academ_iq_api.auth.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.auth.exception.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.user.model.User;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND));
    }

    public String generateAccessToken(User user) {
        Instant expiresAt = Instant.now().plus(1, ChronoUnit.HOURS);
        String tokenType = "access";

        return generateToken(user, expiresAt, tokenType);
    }

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

    public String generateVerificationToken(User user) {
        Instant expiresAt = Instant.now().plus(24, ChronoUnit.HOURS);
        String tokenType = "verify";

        return generateToken(user, expiresAt, tokenType);
    }

    public String generateRecoverPasswordToken(User user) {
        Instant expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
        String tokenType = "recover";

        return generateToken(user, expiresAt, tokenType);
    }

    public String refreshAccessToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(ErrorMessages.REFRESH_TOKEN_NOT_FOUND));

        this.validate(token, "refresh");

        return generateAccessToken(refreshToken.getUser());
    }

    public Jwt decode(String token) {
        token = token.contains(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;

        try {
            return jwtDecoder.decode(token);
        } catch (JwtException ex) {
            throw new InvalidTokenException(ErrorMessages.MALFORMED_TOKEN);
        }
    }

    public String getSubject(String token) {
        token = token.contains(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;

        try {
            return jwtDecoder.decode(token).getSubject();
        } catch (JwtException ex) {
            throw new InvalidTokenException(ErrorMessages.MALFORMED_TOKEN);
        }
    }

    public Instant getExpiresAt(String token) {
        token = token.contains(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;

        try {
            return jwtDecoder.decode(token).getExpiresAt();
        } catch (JwtException ex) {
            throw new InvalidTokenException(ErrorMessages.MALFORMED_TOKEN);
        }
    }

    public String getTokenType(String token) {
        token = token.contains(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;

        try {
            return jwtDecoder.decode(token).getClaimAsString("token_type");
        } catch (JwtException ex) {
            throw new InvalidTokenException(ErrorMessages.MALFORMED_TOKEN);
        }
    }

    public void validate(String token, @Nullable String tokenType) {
        if (token.isBlank() || token == null)
            throw new AuthCredentialsNotFoundException(ErrorMessages.REQUIRED_TOKEN);

        token = token.contains(BEARER_PREFIX) ? token.substring(BEARER_PREFIX.length()) : token;

        Instant expiresAt = this.getExpiresAt(token);
        String type = this.getTokenType(token);

        if (expiresAt == null || expiresAt.isBefore(Instant.now()))
            throw new RefreshTokenExpiredException(ErrorMessages.EXPIRED_TOKEN);

        if (tokenType == null || !tokenType.equals(type))
            throw new InvalidTokenTypeException(ErrorMessages.INVALID_TOKEN_TYPE);
    }

    public void invalidate(String token) {
        if (refreshTokenRepository.existsByToken(token))
            refreshTokenRepository.deleteByToken(token);
    }

    /**
     * Generates a token for the given user with the specified expiration time and
     * token type
     * This method is used to create access, refresh, verification, and password
     * recovery tokens
     *
     * @param user      the user for whom the token is generated
     * @param expiresAt the expiration time of the token as an Instant
     * @param tokenType the type of the token (e.g., "access", "refresh", "verify",
     *                  "recover")
     * @return the generated token as a String
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
