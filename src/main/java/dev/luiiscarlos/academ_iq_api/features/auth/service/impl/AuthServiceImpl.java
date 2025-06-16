package dev.luiiscarlos.academ_iq_api.features.auth.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.luiiscarlos.academ_iq_api.core.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.LoginResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.RegisterResponse;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.Credentials;
import dev.luiiscarlos.academ_iq_api.features.auth.dto.ResetPasswordRequest;
import dev.luiiscarlos.academ_iq_api.features.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.features.auth.mapper.AuthMapper;
import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.features.auth.service.AuthService;
import dev.luiiscarlos.academ_iq_api.features.file.model.File;
import dev.luiiscarlos.academ_iq_api.features.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.features.user.exception.UserNotFoundException;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.user.security.Role;
import dev.luiiscarlos.academ_iq_api.features.user.security.RoleService;
import dev.luiiscarlos.academ_iq_api.features.user.service.impl.UserQueryService;
import dev.luiiscarlos.academ_iq_api.shared.enums.RoleType;
import dev.luiiscarlos.academ_iq_api.shared.mail.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserQueryService userQueryService;

    private final AuthMapper authMapper;

    private final TokenService tokenService;

    private final MailService mailService;

    private final RoleService roleService;

    private final FileService fileService;

    public String refresh(String refreshToken) {
        tokenService.validate(refreshToken, "refresh");

        String refreshedAccessToken = tokenService.refreshAccessToken(refreshToken);
        String username = tokenService.getSubject(refreshToken);

        log.info("User '{}' has successfully refreshed the access token at {}", username, LocalDateTime.now());

        return refreshedAccessToken;
    }

    public void verify(String verifyToken) {
        tokenService.validate(verifyToken, "verify");

        String username = tokenService.getSubject(verifyToken);
        if (!userQueryService.existsByUsername(username))
            throw new UserNotFoundException(String.format(ErrorMessages.TOKEN_SUBJECT_NOT_FOUND, username));

        User user = userQueryService.findByUsername(username);
        user.setVerified(true);

        userQueryService.save(user);

        log.info("User '{}' has successfully verified the account at {}", user.getUsername(), LocalDateTime.now());
    }

    public RegisterResponse register(RegisterRequest request, String origin) {
        if (userQueryService.existsByUsername(request.getUsername()))
            throw new UserAlreadyExistsException(ErrorMessages.INVALID_CREDENTIALS);

        String encodedPassword = BCRYPT_PREFIX + passwordEncoder.encode(request.getPassword());
        Set<Role> authorities = Set.of(roleService.findByAuthority(RoleType.USER));
        File avatar = fileService.get("default-user-avatar_nsfvaz");

        User user = authMapper.toModel(request);
        user.setPassword(encodedPassword);
        user.setAuthorities(authorities);
        user.setAvatar(avatar);

        userQueryService.save(user);

        log.info("User '{}' has successfully signed up at {}", user.getUsername(), LocalDateTime.now());

        mailService.sendVerificationMail(user, origin);

        return authMapper.toRegisterResponse(user);
    }

    public LoginResponse login(Credentials credentials, String origin) {
        User user = userQueryService.findByUsername(credentials.getUsername());

        if (!passwordEncoder.matches(credentials.getPassword(),
                user.getPassword().substring(BCRYPT_PREFIX.length())))
            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);

        if (!user.isVerified()) {
            mailService.sendVerificationMail(user, origin);
            throw new UserAccountNotVerifiedException(
                    String.format(ErrorMessages.USER_NOT_VERIFIED, credentials.getUsername()));
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user).getToken();

        log.info("User '{}' has successfully logged in at {}", credentials.getUsername(), LocalDateTime.now());

        return authMapper.toLoginResponse(user, accessToken, refreshToken);
    }

    public void logout(long userId, String refreshToken) {
        User user = userQueryService.findById(userId);

        if (!tokenService.existsByTokenAndUserId(refreshToken, user.getId()))
            throw new TokenNotFoundException(
                    String.format(ErrorMessages.REFRESH_TOKEN_NOT_ASSOCIATED_WITH_USER, user.getUsername()));

        tokenService.validate(refreshToken, "refresh");
        tokenService.invalidate(refreshToken);

        log.info("User '{}' has successfully logged out at {}", user.getUsername(), LocalDateTime.now());

    }

    public void recoverPassword(String email, String origin) {
        User user = userQueryService.findByEmail(email);

        if (!user.isVerified()) {
            mailService.sendVerificationMail(user, origin);
            throw new UserAccountNotVerifiedException(
                    String.format(ErrorMessages.USER_NOT_VERIFIED, email));
        }

        mailService.sendResetPasswordMail(user, origin);

        log.info("User '{}' has requested to recover the password at {}", user.getUsername(), LocalDateTime.now());
    }

    public void resetPassword(String recoverToken, ResetPasswordRequest request) {
        tokenService.validate(recoverToken, "recover");

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String username = tokenService.getSubject(recoverToken);

        User user = userQueryService.findByUsername(username);
        user.setPassword(BCRYPT_PREFIX + encodedPassword);

        userQueryService.save(user);

        log.info("User '{}' has successfully reseted the password at {}", user.getUsername(), LocalDateTime.now());
    }

}
