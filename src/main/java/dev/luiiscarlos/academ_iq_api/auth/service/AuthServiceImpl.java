package dev.luiiscarlos.academ_iq_api.auth.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.auth.dto.AuthResponse;
import dev.luiiscarlos.academ_iq_api.auth.dto.Credentials;
import dev.luiiscarlos.academ_iq_api.auth.dto.ResetPassword;
import dev.luiiscarlos.academ_iq_api.auth.exception.InvalidCredentialsException;
import dev.luiiscarlos.academ_iq_api.auth.security.TokenService;
import dev.luiiscarlos.academ_iq_api.exception.ErrorMessages;
import dev.luiiscarlos.academ_iq_api.file.model.File;
import dev.luiiscarlos.academ_iq_api.file.service.FileService;
import dev.luiiscarlos.academ_iq_api.shared.mail.MailService;
import dev.luiiscarlos.academ_iq_api.user.exception.UserAccountNotVerifiedException;
import dev.luiiscarlos.academ_iq_api.user.exception.UserAlreadyExistsException;
import dev.luiiscarlos.academ_iq_api.user.mapper.UserMapper;
import dev.luiiscarlos.academ_iq_api.user.model.User;
import dev.luiiscarlos.academ_iq_api.user.security.Role;
import dev.luiiscarlos.academ_iq_api.user.security.RoleService;
import dev.luiiscarlos.academ_iq_api.user.service.UserService;

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

    public AuthResponse login(Credentials credentials, @Nullable String origin) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName() + " [" + authentication.getClass() + "]");
        log.info(authentication.getAuthorities() + " " + authentication.getPrincipal());


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

    public User register(User userToRegister, @Nullable String origin) {
        if (userService.existsByUsername(userToRegister.getUsername()))
            throw new UserAlreadyExistsException(ErrorMessages.INVALID_CREDENTIALS);

        String encodedPassword = ENCODED_PASSWORD_PREFIX + passwordEncoder.encode(userToRegister.getPassword());

        Set<Role> authorities = Set.of(roleService.findByAuthority("USER"));
        File defaultAvatar = fileService.findByFilename("default-user-avatar_nsfvaz");

        userToRegister.setAvatar(defaultAvatar);
        userToRegister.setPassword(encodedPassword);
        userToRegister.setAuthorities(authorities);

        if (origin != null)
            mailService.sendMailVerification(userToRegister, origin);
        else
            userToRegister.setVerified(true);

        log.info("User '%s' has successfully signed up at %s", userToRegister.getUsername(), LocalDateTime.now());

        return userService.save(userToRegister);
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

    public void recoverPassword(String email, String origin) {
        User user = userService.findByEmail(email);

        if (!user.isVerified()) {
            mailService.sendMailVerification(user, origin);
            throw new UserAccountNotVerifiedException(String.format(
                    ErrorMessages.USER_NOT_VERIFIED, email));
        }

        mailService.sendMailPasswordRecover(user, origin);

        log.info("User '%s' has requested to recover the password at %s", user.getUsername(), LocalDateTime.now());
    }

    public void resetPassword(String token, ResetPassword passwordDto) {
        tokenService.validate(token, "recover");

        User user = userService.findByToken(token);

        String encodedPassword = passwordEncoder.encode(passwordDto.getPassword());

        user.setPassword(ENCODED_PASSWORD_PREFIX + encodedPassword);
        userService.save(user);

        log.info("User '%s' has successfully reseted the password at %s", user.getUsername(), LocalDateTime.now());
    }

}
