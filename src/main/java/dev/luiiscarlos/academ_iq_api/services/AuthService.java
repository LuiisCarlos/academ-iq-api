package dev.luiiscarlos.academ_iq_api.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.dtos.UserChangePasswordDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserLoginRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserLoginResponseDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterRequestDto;
import dev.luiiscarlos.academ_iq_api.dtos.UserRegisterResponseDto;
import dev.luiiscarlos.academ_iq_api.exceptions.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.RoleNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserAlreadyRegisteredException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.exceptions.UserRegistrationWithDifferentPasswordsException;
import dev.luiiscarlos.academ_iq_api.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.RefreshToken;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.repositories.RoleRepository;
import dev.luiiscarlos.academ_iq_api.repositories.UserRepository;
import dev.luiiscarlos.academ_iq_api.services.interfaces.IAuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private static final String ENCODED_PASSWORD_PREFIX = "{bcrypt}";

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final TokenService tokenService;

    private final UserMapper userMapper;

    /**
     * Login a user
     *
     * @param loginRequest The user to login
     * @return The logged in user
     */
    @Override
    public UserLoginResponseDto login(UserLoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new UserNotFoundException());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException(); // TODO: Log this
        }

        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);

        return userMapper.mapToUserLoginResponseDto(user, accessToken, refreshToken.getToken(), refreshToken.getExpiresAt());
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
            throw new UserRegistrationWithDifferentPasswordsException();

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent())
            throw new UserAlreadyRegisteredException();

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        Role userRole = roleRepository.findByAuthority("USER")
            .orElseThrow(() -> new RoleNotFoundException());

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        User user = userMapper.mapToUser(registerRequest, encodedPassword, authorities);

        return userMapper.mapToUserRegisterResponseDto(userRepository.save(user));
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
            throw new AuthenticationCredentialsNotFoundException("Refresh token is required");

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
    public void logout(HttpServletRequest request, HttpServletResponse response, String tokenJson) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = tokenService.extractTokenFromJson(tokenJson);

        if (token == null)
            throw new AuthenticationCredentialsNotFoundException("Refresh token is required");

        String username = tokenService.extractUsernameFromToken(token);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException());

        RefreshToken refreshToken = tokenService.findByToken(token);

        if (!refreshToken.getUser().getId().equals(user.getId())) {
            throw new InvalidCredentialsException(); // * <- Invalid refresh token
        }

        tokenService.invalidateRefreshToken(refreshToken.getToken());

        new SecurityContextLogoutHandler().logout(request, response, authentication);

        SecurityContextHolder.clearContext();
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
    public void changePassword(UserChangePasswordDto changePassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException("Invalid old password");

        if (!changePassword.getNewPassword().equals(changePassword.getConfirmNewPassword()))
            throw new UserRegistrationWithDifferentPasswordsException();

        user.setPassword("{bcrypt}" + passwordEncoder.encode(changePassword.getNewPassword()));

        userRepository.save(user);
    }

}
