package dev.luiiscarlos.academ_iq_api.services;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.exceptions.auth.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.exceptions.user.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.models.File;
import dev.luiiscarlos.academ_iq_api.models.Role;
import dev.luiiscarlos.academ_iq_api.models.User;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.Credentials;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.PasswordResetDto;
import dev.luiiscarlos.academ_iq_api.models.dtos.user.LoginResponseDto;
import dev.luiiscarlos.academ_iq_api.models.mappers.UserMapper;
import dev.luiiscarlos.academ_iq_api.services.interfaces.AuthService;
import dev.luiiscarlos.academ_iq_api.services.interfaces.TokenService;
import dev.luiiscarlos.academ_iq_api.services.interfaces.UserService;

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

    public LoginResponseDto login(Credentials credentials, @Nullable String origin) {
        User user = userService.findByUsername(credentials.getUsername());

        if (!passwordEncoder.matches(credentials.getPassword(),
                user.getPassword().substring(ENCODED_PASSWORD_PREFIX.length())))
            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);

        if (!user.isVerified() && origin != null) {
            mailService.sendMailVerification(user, origin);
            throw new UserAccountNotVerifiedException(String.format(
                    ErrorMessages.USER_NOT_VERIFIED, credentials.getUsername()));
        }

        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user).getToken();

        log.info("User '%s' has successfully logged in at %s", credentials.getUsername(), LocalDateTime.now());

        return userMapper.toLoginResponseDto(accessToken, refreshToken, user);
    }

    public User register(User register, @Nullable String origin) {
        if (userService.existsByUsername(register.getUsername()))
            throw new UserAlreadyExistsException(ErrorMessages.INVALID_CREDENTIALS);

        String encodedPassword = ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(register.getPassword());

        Set<Role> authorities = Set.of(roleService.findByAuthority("USER"));
        File defaultAvatar = fileService.findByFilename("default-user-avatar_nsfvaz");

        register.setAvatar(defaultAvatar);
        register.setPassword(encodedPassword);
        register.setAuthorities(authorities);

        if (origin != null)
            mailService.sendMailVerification(register, origin);
        else
            register.setVerified(true);

        log.info("User '%s' has successfully signed up at %s", register.getUsername(), LocalDateTime.now());

        return userService.save(register);
    }

    public String refresh(String token) {
        tokenService.validate(token, "refresh");

        log.info("User '%s' has successfully refreshed the access token at %s",
                tokenService.getSubject(token), LocalDateTime.now());

        return tokenService.refreshAccessToken(token);
    }

    public void logout(String token) {
        tokenService.validate(token, "refresh");

        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        String refreshToken = tokenService.findByToken(token).getToken();
        User user = userService.findByToken(token);

        tokenService.invalidate(refreshToken);

        log.info("User '%s' has successfully logged out at %s", user.getUsername(), LocalDateTime.now());
    }

    public void verify(String token) {
        tokenService.validate(token, "verify");

        User user = userService.findByToken(token);

        user.setVerified(true);
        userService.save(user);

        log.info("User '%s' has successfully verified the account at %s", user.getUsername(), LocalDateTime.now());
    }

    public void recoverPassword(String email, @Nullable String origin) {
        User user = userService.findByEmail(email);

        if (!user.isVerified() && origin != null) {
            mailService.sendMailVerification(user, origin);
            throw new UserAccountNotVerifiedException(String.format(
                    ErrorMessages.USER_NOT_VERIFIED, email));
        }

        mailService.sendMailPasswordRecover(user, origin);

        log.info("User '%s' has requested to recover the password at %s", user.getUsername(), LocalDateTime.now());
    }

    public void resetPassword(String token, PasswordResetDto passwordDto) {
        tokenService.validate(token, "recover");

        User user = userService.findByToken(token);

        String encodedPassword = passwordEncoder.encode(passwordDto.getPassword());

        user.setPassword(ENCODED_PASSWORD_PREFIX + encodedPassword);
        userService.save(user);

        log.info("User '%s' has successfully reseted the password at %s", user.getUsername(), LocalDateTime.now());
    }

}
