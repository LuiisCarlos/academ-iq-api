package dev.luiiscarlos.academ_iq_api.services;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.AuthCredentialsNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidTokenException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserAlreadyRegisteredException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserUnderageException;
import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserChangePasswordDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.UserResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.interfaces.AuthService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

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

    private final UserMapper userMapper;

    /**
     * Login a user
     *
     * @param loginRequest The user to login
     * @return The logged in user
     */
    @Override
    public UserLoginResponseDto login(UserLoginRequestDto loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());

        if (!passwordEncoder.matches(loginRequest.getPassword(),
                user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException("Failed to login: Invalid username or password");

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);

        return userMapper.toUserLoginResponseDto(user, accessToken, refreshToken.getToken(), refreshToken.getExpiresAt());
    }

    /**
     * Register a new user
     *
     * @param registerRequest The user to register
     * @return The registered user
     */
    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto registerRequest) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword()))
            throw new UserWithDifferentPasswordsException("Failed to register user: Passwords do not match");

        if (userService.existsByUsername(registerRequest.getUsername()))
            throw new UserAlreadyRegisteredException("Failed to register user: Invalid username or password");

        if (LocalDate.parse(registerRequest.getBirthdate()).isAfter(LocalDate.now().minusYears(18)))
            throw new UserUnderageException("Failed to register user: User is underage");

        String encodedPassword = ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(registerRequest.getPassword());

        Role userRole = roleService.findByAuthority("USER");
        Set<Role> authorities = Set.of(userRole);

        User user = userMapper.toUser(registerRequest, encodedPassword, authorities);

        emailService.sendConfirmationEmail(user);

        return userMapper.toUserRegisterResponseDto(userService.save(user));
    }

    /**
     * Refresh the access token
     *
     * @param tokenJson The refresh token
     * @return The new access token
     */
    @Override
    public String refresh(String tokenJson) {
        String token = tokenService.extractTokenFromJson(tokenJson);

        if (token == null)
            throw new AuthCredentialsNotFoundException(
                "Failed to refresh access token: Refresh token is required");

        return tokenService.refreshAccessToken(token);
    }

    /**
     * Logout the user and invalidate the refresh token
     *
     * @param request The request
     * @param response The response
     * @param tokenJson The refresh token
     */
    @Override
    @SuppressWarnings("null") // TODO: Review this
    public void logout(String tokenJson) {
        String token = tokenService.extractTokenFromJson(tokenJson);

        if (token == null)
            throw new AuthCredentialsNotFoundException("Failed to logout: Refresh token is required");

        String username = tokenService.extractUsernameFromToken(token);
        User user = userService.findByUsername(username);

        RefreshToken refreshToken = tokenService.findByToken(token);

        if (!refreshToken.getUser().getId().equals(user.getId()))
            throw new InvalidTokenException("Failed to logout: Invalid refresh token");

        tokenService.invalidateRefreshToken(refreshToken.getToken());
    }

    /**
     * Verifies the user's email
     *
     * @param token The token
     * @return The user
     */
    @Override
    public UserResponseDto verify(String token) {
        if (!tokenService.isValidToken(token))
            throw new InvalidTokenException("Failed to verify the email: Invalid token");

        String username = tokenService.extractUsernameFromToken(token);

        User user = userService.findByUsername(username);
        user.setIsEmailVerified(true);

        return userMapper.toUserResponseDto(userService.save(user));
    }

    @Override
    public String recoverPassword(String email) {
        // ? QST: Implement this
        throw new UnsupportedOperationException("Unimplemented method 'recoverPassword'");
    }

    /**
     * Change the password of the current user
     *
     * @param changePassword The new password and the old password
     */
    @Override
    public void changePassword(String token, UserChangePasswordDto changePassword) {
        token = token.substring(ENCODED_PASSWORD_PREFIX.length());
        String username = tokenService.extractUsernameFromToken(token);

        User user = userService.findByUsername(username);

        if (!passwordEncoder.matches(changePassword.getOldPassword(),
                user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException("Failed to change password: Invalid old password");

        if (!changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword()))
            throw new UserWithDifferentPasswordsException("Failed to change password: Passwords do not match");

        user.setPassword(ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(changePassword.getNewPassword()));
        userService.save(user);
    }

}
