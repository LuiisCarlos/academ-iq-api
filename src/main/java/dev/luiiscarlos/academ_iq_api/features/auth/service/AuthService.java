package dev.luiiscarlos.academ_iq_api.features.auth.service;

import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.Credentials;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.ResetPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;

public interface AuthService {

    static final String ENCODED_PASSWORD_PREFIX = "{bcrypt}";

    /**
     * Refresh the access token for the refresh token given
     *
     * @param refreshToken the refresh token
     * @return the new access token
     */
    String refresh(String refreshToken);

    /**
     * Verifies the user with a verify token
     *
     * @param verifyToken the token
     * @throws UserNotFoundException if the user its not found with the token
     *                               subject
     */
    void verify(String verifyToken);

    /**
     * Signs up a new user
     *
     * @param request {@link RegisterRequest} the user to register
     * @param origin  the origin of the request
     * @return {@link User} the registered user
     * @throws UserAlreadyExistsException if the username already exists
     */
    RegisterResponse register(RegisterRequest request, String origin);

    /**
     * Logs in a user using its username and password
     *
     * @param credentials {@link Credentials} the credentials of the user
     * @param origin      the origin of the request
     * @return {@link LoginResponse} the logged in user
     * @throws UserAccountNotVerifiedException if the user account is not verified
     * @throws InvalidCredentialsException     if the username or password is
     *                                         invalid
     */
    LoginResponse login(Credentials credentials, String origin);

    /**
     * Logs out the current user and invalidates its refresh token
     *
     * @param refreshToken the refresh token
     * @throws TokenNotFoundException if there is no associated token with the
     *                                user in the database
     */
    void logout(String refreshToken);

    /**
     * If the user account is verified, sends an email to recover its password
     *
     * @param email  the email of the user
     * @param origin the origin of the request
     * @throws UserAccountNotVerifiedException if the user account is not verified
     */
    void recoverPassword(String email, String origin);

    /**
     * Changes the password of the current user
     *
     * @param recoverToken the recover token
     * @param request      {@link ResetPasswordRequest} the new password and its
     *                     confirmation
     */
    void resetPassword(String recoverToken, ResetPasswordRequest request);

}
