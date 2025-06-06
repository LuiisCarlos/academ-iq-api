package dev.luiiscarlos.academ_iq_api.services.interfaces;

import org.springframework.lang.Nullable;

import dev.luiiscarlos.academ_iq_api.exceptions.auth.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.auth.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.token.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserUnderageException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.Credentials;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.LoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordResetDto;

public interface AuthService {

    static final String ENCODED_PASSWORD_PREFIX = "{bcrypt}";

    /**
     * Login a user
     *
     * @param origin  the origin of the request
     * @param userDto the user to login
     * @return the logged in user
     * @throws UserAccountNotVerifiedException if the user account is not verified
     * @throws InvalidCredentialsException     if the username or password is
     *                                         invalid
     */
    LoginResponseDto login(Credentials credentials, @Nullable String origin);

    /**
     * Register a new user
     *
     * @param origin  the origin of the request
     * @param userDto the user to register
     * @return the registered user
     * @throws UserWithDifferentPasswordsException if the password and its
     *                                             confirmation do not match
     * @throws UserAlreadyExistsException          if the username already exists
     * @throws UserUnderageException               if the user is underage
     */
    User register(User register, @Nullable String origin);

    /**
     * Refresh the access token
     *
     * @param token the refresh token
     * @return the new access token
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    String refresh(String token);

    /**
     * Logout the user and invalidate the refresh token
     *
     * @param token the refresh token
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    void logout(String token);

    /**
     * Verifies the user's email
     *
     * @param token the token
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    void verify(String token);

    /**
     * If the user account is verified, can recover his password by its email
     *
     * @param origin the origin of the request
     * @param email  the user's email
     * @throws UserAccountNotVerifiedException if the user account is not verified
     */
    void recoverPassword(String email, @Nullable String origin);

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
    void resetPassword(String token, PasswordResetDto passwordDto);

}
