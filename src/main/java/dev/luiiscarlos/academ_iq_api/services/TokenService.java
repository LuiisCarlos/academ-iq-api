package dev.luiiscarlos.academ_iq_api.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
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

import dev.luiiscarlos.academ_iq_api.models.Role;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private String generateToken(String username, Set<Role> authorities, Instant expiresAt) {
        Instant now = Instant.now();

        String scope = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(username)
                .claim("roles", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateAccessToken(String username, Set<Role> authorities) {
        return generateToken(username, authorities, Instant.now().plus(15, ChronoUnit.MINUTES));
    }

    public String generateRefreshToken(String username, Set<Role> authorities) {
        return generateToken(username, authorities, Instant.now().plus(30, ChronoUnit.DAYS));
    }

    public boolean validateRefreshToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return jwtDecoder.decode(token).getClaimAsString("sub");
    }

    public String getTokenFromRequest(String request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(request);
            String refreshToken = rootNode.path("refreshToken").asText();
            return refreshToken;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
