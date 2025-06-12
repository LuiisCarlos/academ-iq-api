package dev.luiiscarlos.academ_iq_api.features.auth.service;

import org.springframework.lang.Nullable;

import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.Credentials;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.ResetPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.exception.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.features.auth.security.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserUnderageException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;

public interface AuthService {

    static final String ENCODED_PASSWORD_PREFIX = "{bcrypt}";

    /**
     * Logs in a user using its username and password
     *
     * @param credentials the user to login
     * @param origin      the origin of the request
     * @return {@link LoginResponse} the logged in user
     * @throws UserAccountNotVerifiedException if the user account is not verified
     * @throws InvalidCredentialsException     if the username or password is
     *                                         invalid
     */
    LoginResponse login(Credentials credentials, @Nullable String origin);

    /**
     * Signs up a new user
     *
     * @param userToRegister the user to register
     * @param origin         the origin of the request
     * @return {@link User} the registered user
     * @throws UserWithDifferentPasswordsException if the password and its
     *                                             confirmation do not match
     * @throws UserAlreadyExistsException          if the username already exists
     * @throws UserUnderageException               if the user is underage
     */
    RegisterResponse register(RegisterRequest request, @Nullable String origin);

    /**
     * Refresh the access token for the current user
     *
     * @param token the refresh token
     * @return the new access token
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    String refresh(String token);

    /**
     * Logout the current user and invalidate the refresh token
     *
     * @param token the refresh token
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    void logout(String token);

    /**
     * Verifies the user's account by a token
     *
     * @param token the token
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    void verify(String token);

    /**
     * If the user account is verified, can recover his password by its email
     *
     * @param email  the user's email
     * @param origin the origin of the request
     * @throws UserAccountNotVerifiedException if the user account is not verified
     */
    void recoverPassword(String email, String origin);

    /**
     * Change the password of the current user
     *
     * @param token       the recover token
     * @param passwordDto the new password and its confirmation
     * @throws AuthCredentialsNotFoundException    if the token is null or blank
     * @throws InvalidTokenException               if the token is invalid or
     *                                             expired
     * @throws UserWithDifferentPasswordsException if the password and its
     *                                             confirmation do not match
     */
    void resetPassword(String token, ResetPasswordRequest passwordDto);

}
