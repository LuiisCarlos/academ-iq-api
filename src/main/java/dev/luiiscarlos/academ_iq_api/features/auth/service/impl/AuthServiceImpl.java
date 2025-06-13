package dev.luiiscarlos.academ_iq_api.features.auth.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.Credentials;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.ResetPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.features.auth.service.AuthService;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.user.security.Role;
import dev.luiiscarlos.academ_iq_api.features.user.security.RoleService;
import dev.luiiscarlos.academ_iq_api.features.user.service.UserService;
import dev.luiiscarlos.academ_iq_api.shared.mail.MailService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final TokenService tokenService;

    private final MailService mailService;

    private final RoleService roleService;

    private final FileService fileService;

    private final UserMapper userMapper;

    public LoginResponse login(Credentials credentials, @Nullable String origin) {
        User user = userService.findByUsername(credentials.getUsername());

        if (!passwordEncoder.matches(credentials.getPassword(),
                user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);

        if (!user.isVerified() && origin != null) {
            mailService.sendVerificationMail(user, origin);
            throw new UserAccountNotVerifiedException(String.format(
                    ErrorMessages.USER_NOT_VERIFIED, credentials.getUsername()));
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user).getToken();

        log.info(String.format(
                "User '%s' has successfully logged in at %s", credentials.getUsername(), LocalDateTime.now()));

        return userMapper.toLoginResponse(user, accessToken, refreshToken);
    }

    public RegisterResponse register(RegisterRequest request, @Nullable String origin) {
        if (userService.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException(ErrorMessages.INVALID_CREDENTIALS);

        String encodedPassword = ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(request.getPassword());
        Set<Role> authorities = Set.of(roleService.findByAuthority("USER"));
        File avatar = fileService.findByFilename("default-user-avatar_nsfvaz");

        User user = userMapper.toModel(request);
        user.setPassword(encodedPassword);
        user.setAuthorities(authorities);
        user.setAvatar(avatar);

        User saved = userService.save(user);

        if (origin != null)
            mailService.sendVerificationMail(saved, origin);

        log.info(String.format("User '%s' has successfully signed up at %s", saved.getUsername(), LocalDateTime.now()));

        return userMapper.toRegisterResponse(saved);
    }

    public String refresh(String token) {
        tokenService.validate(token, "refresh");

        log.info(String.format("User '%s' has successfully refreshed the access token at %s",
                tokenService.getSubject(token), LocalDateTime.now()));

        return tokenService.refreshAccessToken(token);
    }

    public void logout(String token) {
        tokenService.validate(token, "refresh");

        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        String refreshToken = tokenService.findByToken(token).getToken();
        User user = userService.findByToken(token);

        tokenService.invalidate(refreshToken);

        log.info(String.format("User '%s' has successfully logged out at %s", user.getUsername(), LocalDateTime.now()));
    }

    public void verify(String token) {
        tokenService.validate(token, "verify");

        User user = userService.findByToken(token);
        user.setVerified(true);

        userService.save(user);

        log.info(String.format(
                "User '%s' has successfully verified the account at %s", user.getUsername(), LocalDateTime.now()));
    }

    public void recoverPassword(String email, String origin) {
        User user = userService.findByEmail(email);

        if (!user.isVerified()) {
            mailService.sendVerificationMail(user, origin);
            throw new UserAccountNotVerifiedException(String.format(
                    ErrorMessages.USER_NOT_VERIFIED, email));
        }

        mailService.sendPasswordResetMail(user, origin);

        log.info(String.format(
                "User '%s' has requested to recover the password at %s", user.getUsername(), LocalDateTime.now()));
    }

    public void resetPassword(String token, ResetPasswordRequest request) {
        tokenService.validate(token, "recover");

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = userService.findByToken(token);
        user.setPassword(ENCODED_PASSWORD_PREFIX + encodedPassword);

        userService.save(user);

        log.info(String.format(
                "User '%s' has successfully reseted the password at %s", user.getUsername(), LocalDateTime.now()));
    }

}
