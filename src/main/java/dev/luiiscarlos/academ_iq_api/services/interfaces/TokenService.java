package dev.luiiscarlos.academ_iq_api.services.interfaces;

import java.time.Instant;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.jwt.Jwt;

import dev.luiiscarlos.academ_iq_api.exceptions.auth.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.auth.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.token.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.exceptions.token.RefreshTokenExpiredException;
import dev.luiiscarlos.academ_iq_api.exceptions.token.RefreshTokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.User;

public interface TokenService {

    static final String BEARER_PREFIX = "Bearer ";

    /**
     * Finds a refresh token by its value
     *
     * @param token the value of the refresh token to be found
     * @return {@link RefreshToken} the found RefreshToken object
     * @throws RefreshTokenNotFoundException if the refresh token is not found in
     *                                       the database
     */
    RefreshToken findByToken(String token);

    /**
     * Generates an access token for the given user
     *
     * @param {@link User} the user for whom the access token is generated
     * @return the generated access token as a String
     */
    String generateAccessToken(User user);

    /**
     * Generates a refresh token for the given user
     * If a refresh token already exists for the user, it updates the existing one
     *
     * @param {@link User} the user for whom the refresh token is generated
     * @return {@link RefreshToken} the generated refresh token
     */
    RefreshToken generateRefreshToken(User user);

    /**
     * Generates a verification token for the given user
     * This token is used for email verification
     *
     * @param {@link User} the user for whom the verification token is generated
     * @return the generated verification token as a String
     */
    String generateVerificationToken(User user);

    /**
     * Generates a password recovery token for the given user
     * This token is used for password recovery
     *
     * @param {@link User} the user for whom the password recovery token is
     *               generated
     * @return the generated password recovery token as a String
     */
    String generateRecoverPasswordToken(User user);

    /**
     * Refreshes the access token using the provided refresh token
     *
     * @param token The refresh token used to generate a new access token
     * @return the new access token as a String
     * @throws RefreshTokenNotFoundException if the refresh token is not found in
     * @throws InvalidCredentialsException   if the refresh token is invalid or not
     * @throws RefreshTokenExpiredException  if the refresh token has expired
     */
    String refreshAccessToken(String token);

    /**
     * Gets the Jwt object from the provided token
     *
     * @param token The token to be decoded
     * @return the decoded Jwt object
     * @throws InvalidTokenException if the token is malformed or invalid
     */
    Jwt decode(String token);

    /**
     * Gets the subject of the provided token
     *
     * @param token The token whose subject is to be retrieved
     * @return the subject of the token as a String
     * @throws InvalidTokenException if the token is malformed or invalid
     */
    String getSubject(String token);

    /**
     * Gets the expiration time of the provided token
     *
     * @param token The token whose expiration time is to be retrieved
     * @return the expiration time of the token as an Instant
     * @throws InvalidTokenException if the token is malformed or invalid
     */
    Instant getExpiresAt(String token);

    /**
     * Finds a refresh token by its ID
     *
     * @param id The id of the refresh token to be found
     * @return the found RefreshToken object
     * @throws RefreshTokenNotFoundException if the refresh token is not found in
     *                                       the database
     */
    String getTokenType(String token);

    /**
     * Validates the provided token
     *
     * @param token     the token to be validated
     * @param tokenType the token type to custom validate
     * @return true if the token is valid, false otherwise
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidCredentialsException      if the token is invalid or not
     * @throws RefreshTokenExpiredException     if the token has expired
     */
    void validate(String token, @Nullable String tokenType);

    /**
     * Invalidates the provided token
     *
     * @param the token to be invalidated
     * @param the token type to be compared
     */
    void invalidate(String token);

}
