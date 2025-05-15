package dev.luiiscarlos.academ_iq_api.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserUnderageException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordResetDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.interfaces.AuthService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String ENCODED_PASSWORD_PREFIX = "{bcrypt}";

    private final PasswordEncoder passwordEncoder;

    private final UserServiceImpl userService;

    private final TokenServiceImpl tokenService;

    private final MailService mailService;

    private final RoleService roleService;

    private final FileServiceImpl fileService;

    private final UserMapper userMapper;

    /**
     * Login a user
     *
     * @param origin  the origin of the request
     * @param userDto the user to login
     *
     * @return the logged in user
     *
     * @throws UserAccountNotVerifiedException if the user account is not verified
     * @throws InvalidCredentialsException     if the username or password is
     *                                         invalid
     */
    @SuppressWarnings("null")
    public UserLoginResponseDto login(String origin, UserLoginRequestDto userDto) {
        User user = userService.findByUsername(userDto.getUsername());
        if (!user.isVerified()) {
            mailService.sendEmailVerification(origin, user);
            throw new UserAccountNotVerifiedException(
                    "Failed to login: User account is not verified");
        }

        if (!passwordEncoder.matches(userDto.getPassword(),
                user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException("Failed to login: Invalid username or password");

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);

        log.debug("User " + user.getUsername() + " has successfully logged in at " + LocalDateTime.now());

        return userMapper.toUserLoginResponseDto(accessToken, refreshToken.getToken(), user);
    }

    /**
     * Register a new user
     *
     * @param origin  the origin of the request
     * @param userDto the user to register
     *
     * @return the registered user
     *
     * @throws UserWithDifferentPasswordsException if the password and its
     *                                             confirmation do not match
     * @throws UserAlreadyExistsException          if the username already exists
     * @throws UserUnderageException               if the user is underage
     */
    public UserRegisterResponseDto register(String origin, UserRegisterRequestDto userDto) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword()))
            throw new UserWithDifferentPasswordsException(
                    "Failed to register user: Passwords do not match");

        if (userService.existsByUsername(userDto.getUsername()))
            throw new UserAlreadyExistsException(
                    "Failed to register user: Invalid username or password");

        if (LocalDate.parse(userDto.getBirthdate()).isAfter(LocalDate.now().minusYears(18)))
            throw new UserUnderageException("Failed to register user: User is underage");

        String encodedPassword = ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(userDto.getPassword());

        Role userRole = roleService.findByAuthority("USER");
        File defaltAvatar = fileService.findByFilename("default-user-avatar_nsfvaz");

        User user = userMapper.toUser(userDto, encodedPassword, Set.of(userRole), defaltAvatar);

        mailService.sendEmailVerification(origin, user);

        log.debug("User " + user.getUsername() + " has successfully signed up at " + LocalDateTime.now());

        return userMapper.toUserRegisterResponseDto(userService.save(user));
    }

    /**
     * Refresh the access token
     *
     * @param token the refresh token
     *
     * @return the new access token
     *
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    public String refresh(String token) {
        if (token == null || token.isBlank())
            throw new AuthCredentialsNotFoundException(
                    "Failed to refresh access token: Refresh token is required");

        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException(
                    "Failed to refresh access token: Refresh token is expired");

        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException(
                    "Failed to refresh access token: Invalid refresh token");

        log.debug("User " + tokenService.getTokenSubject(token) +
                " has successfully refreshed the access token at " + LocalDateTime.now());

        return tokenService.refreshAccessToken(token);
    }

    /**
     * Logout the user and invalidate the refresh token
     *
     * @param token the refresh token
     *
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    @SuppressWarnings("null") // Already handled
    public void logout(String token) {
        if (token == null || token.isBlank())
            throw new AuthCredentialsNotFoundException(
                    "Failed to logout: Refresh token is required");

        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        RefreshToken refreshToken = tokenService.findByToken(token);
        String username = tokenService.getTokenSubject(token);
        User user = userService.findByUsername(username);

        if (!refreshToken.getUser().getId().equals(user.getId()))
            throw new InvalidTokenException("Failed to logout: Invalid refresh token");

        tokenService.invalidateRefreshToken(refreshToken.getToken());

        log.debug("User " + user.getUsername() + " has successfully logged out at " + LocalDateTime.now());
    }

    /**
     * Verifies the user's email
     *
     * @param token the token
     *
     * @throws AuthCredentialsNotFoundException if the token is null or blank
     * @throws InvalidTokenException            if the token is invalid or expired
     */
    public void verify(String token) {
        if (token == null || token.isBlank())
            throw new AuthCredentialsNotFoundException(
                    "Failed to logout: Refresh token is required");

        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException("Failed to verify the email: Invalid token");

        if (!"verify".equals(tokenService.getTokenType(token)))
            throw new InvalidTokenException("Failed to verify the email: Invalid token type");

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException("Failed to verify the email: Token is expired");

        String username = tokenService.getTokenSubject(token);

        User user = userService.findByUsername(username);

        user.setIsVerified(true);
        userService.save(user);

        log.debug("User " + user.getUsername() +
                " has successfully verified the account at " + LocalDateTime.now());
    }

    /**
     * If the user account is verified, can recover his password by its email
     *
     * @param origin the origin of the request
     * @param email  the user's email
     *
     * @throws UserAccountNotVerifiedException if the user account is not verified
     */
    public void recoverPassword(String origin, String email) {
        User user = userService.findByEmail(email);

        if (!user.isVerified()) {
            mailService.sendEmailVerification(origin, user);
            throw new UserAccountNotVerifiedException(
                    "Failed to recover password: User account is not verified. " +
                            "An email has been sent to verify the account");
        }

        mailService.sendEmailPasswordRecover(origin, user);

        log.debug("User " + user.getUsername() +
                " has requested to recover the password at " + LocalDateTime.now());
    }

    /**
     * Change the password of the current user
     *
     * @param token       the recover token
     * @param passwordDto the new password and its confirmation
     *
     * @throws AuthCredentialsNotFoundException    if the token is null or blank
     * @throws InvalidTokenException               if the token is invalid or
     *                                             expired
     * @throws UserWithDifferentPasswordsException if the password and its
     *                                             confirmation do not match
     */
    public void resetPassword(String token, PasswordResetDto passwordDto) {
        if (token == null || token.isBlank())
            throw new AuthCredentialsNotFoundException(
                    "Failed to reset password: Recover token is required");

        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException("Failed to reset password: Invalid token");

        if (!"recover".equals(tokenService.getTokenType(token)))
            throw new InvalidTokenException("Failed to reset password: Invalid token type");

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException("Failed to reset password:Token is expired");

        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword()))
            throw new UserWithDifferentPasswordsException(
                    "Failed to reset password: Passwords do not match");

        String username = tokenService.getTokenSubject(token);
        User user = userService.findByUsername(username);

        user.setPassword(ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(passwordDto.getPassword()));
        userService.save(user);

        log.debug("User " + user.getUsername() +
                " has successfully reset the password at " + LocalDateTime.now());
    }

}
