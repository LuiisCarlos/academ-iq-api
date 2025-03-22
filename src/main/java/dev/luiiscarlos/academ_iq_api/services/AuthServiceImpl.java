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
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserResetPasswordDto;
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

    private final EmailService emailService;

    private final RoleService roleService;

    private final FileServiceImpl fileService;

    private final UserMapper userMapper;

    /**
     * Login a user
     *
     * @param origin the origin of the request
     * @param loginRequest the user to login
     * @return the logged in user
     */
    public UserLoginResponseDto login(String origin, UserLoginRequestDto loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());

        if (!user.isVerified()) {
            emailService.sendEmailVerification(origin, user);
            throw new UserAccountNotVerifiedException("Failed to login: User account is not verified");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(),
                user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException("Failed to login: Invalid username or password");

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);

        log.info("User " + user.getUsername() + " has successfully logged in at " + LocalDateTime.now());
        return userMapper.toUserLoginResponseDto(user, accessToken, refreshToken.getToken(), refreshToken.getExpiresAt());
    }

    /**
     * Register a new user
     *
     * @param origin the origin of the request
     * @param registerRequest the user to register
     * @return the registered user
     */
    public UserRegisterResponseDto register(String origin, UserRegisterRequestDto registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword()))
            throw new UserWithDifferentPasswordsException("Failed to register user: Passwords do not match");

        if (userService.existsByUsername(registerRequest.getUsername()))
            throw new UserAlreadyExistsException("Failed to register user: Invalid username or password");

        if (LocalDate.parse(registerRequest.getBirthdate()).isAfter(LocalDate.now().minusYears(18)))
            throw new UserUnderageException("Failed to register user: User is underage");

        String encodedPassword = ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(registerRequest.getPassword());

        Role userRole = roleService.findByAuthority("USER");
        File defaltAvatar = fileService.findByFilename("default-user-avatar.png");

        User user = userMapper.toUser(registerRequest, encodedPassword, Set.of(userRole), defaltAvatar);

        emailService.sendEmailVerification(origin, user);

        return userMapper.toUserRegisterResponseDto(userService.save(user));
    }

    /**
     * Refresh the access token
     *
     * @param tokenJson the refresh token
     * @return the new access token
     */
    public String refresh(String token) {
        token = token.contains("{") ? tokenService.extractTokenFromJson(token) : token;

        if (token == null)
            throw new AuthCredentialsNotFoundException(
                "Failed to refresh access token: Refresh token is required");

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException("Failed to refresh access token: Refresh token is expired");

        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException("Failed to refresh access token: Invalid refresh token");

        return tokenService.refreshAccessToken(token);
    }

    /**
     * Logout the user and invalidate the refresh token
     *
     * @param token the refresh token
     */
    @SuppressWarnings("null") // TODO: Review this
    public void logout(String token) {
        if (token == null)
            throw new AuthCredentialsNotFoundException("Failed to logout: Refresh token is required");

        token = token.contains("{") ? tokenService.extractTokenFromJson(token) : token;
        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        RefreshToken refreshToken = tokenService.findByToken(token);
        String username = tokenService.getTokenSubject(token);
        User user = userService.findByUsername(username);

        if (!refreshToken.getUser().getId().equals(user.getId()))
            throw new InvalidTokenException("Failed to logout: Invalid refresh token");

        tokenService.invalidateRefreshToken(refreshToken.getToken());
        log.info("User " + user.getUsername() + " has successfully logged out at " + LocalDateTime.now());
    }

    /**
     * Verifies the user's email
     *
     * @param token the token
     */
    public void verify(String token) {
        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException("Failed to verify the email: Invalid token");

        if (!"verify".equals(tokenService.getTokenType(token)))
            throw new InvalidTokenException("Failed to verify the email: Invalid token type");

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException("Failed to verify the email: Token is expired");

        String username = tokenService.getTokenSubject(token);

        User user = userService.findByUsername(username);
        user.setVerified(true);
        userService.save(user);
        log.info("User " + user.getUsername() + " has successfully verified the account at " + LocalDateTime.now());
    }

    /**
     *  If the user account is verified, can recover his password by its email
     *
     * @param origin the origin of the request
     * @param email the user's email
     */
    public void recoverPassword(String origin, String email) {
        User user = userService.findByEmail(email);

        if (!user.isVerified())
            throw new UserAccountNotVerifiedException("Failed to recover password: User's account is not verified");

        emailService.sendEmailPasswordRecover(origin, user);
    }

    /**
     * Change the password of the current user
     *
     * @param token the recover token
     * @param resetPassword the new password and its confirmation
     */
    public void resetPassword(String token, UserResetPasswordDto resetPassword) {
        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException("Failed to verify the email: Invalid token");

        if (!"recover".equals(tokenService.getTokenType(token)))
            throw new InvalidTokenException("Failed to verify the email: Invalid token type");

        if (tokenService.getTokenExpiration(token).isBefore(Instant.now()))
            throw new InvalidTokenException("Failed to verify the email: Token is expired");

        String username = tokenService.getTokenSubject(token);

        User user = userService.findByUsername(username);

        if (!resetPassword.getPassword().equals(resetPassword.getConfirmPassword()))
            throw new UserWithDifferentPasswordsException("Failed to change password: Passwords do not match");

        user.setPassword(ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(resetPassword.getPassword()));
        userService.save(user);
    }

}
