package dev.luiiscarlos.academ_iq_api.shared.mail;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.features.auth.security.TokenServiceImpl;
import dev.luiiscarlos.academ_iq_api.features.user.model.User;
import dev.luiiscarlos.academ_iq_api.shared.mail.exception.MailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final TokenServiceImpl tokenService;

    private final JavaMailSender mailSender;

    private final MailTemplateProcessor mailTemplateProcessor;

    public void sendVerificationMail(User user, String origin) {
        String verificationUrl = generateUrl(origin, "auth/verify", tokenService.generateVerificationToken(user));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Map<String, Object> vars = new HashMap<>();
            vars.put("verificationUrl", verificationUrl);

            String htmlContent = mailTemplateProcessor.buildTemplate("verification.html", vars);

            helper.setTo(user.getEmail());
            helper.setSubject("[Academ-IQ] - 🔒 Verify your account");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Failed to send the confirmation email", ex.getCause());
            throw new MailSendingException("Failed to send the confirmation email: " + ex.getMessage());
        }

        log.info(String.format(
                "A verification e-mail has been sent to '%s' at %s", user.getUsername(), LocalDateTime.now()));
    }

    public void sendPasswordResetMail(User user, String origin) {
        String passwordResetUrl = generateUrl(origin, "auth/reset-password",
                tokenService.generateRecoverPasswordToken(user));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Map<String, Object> vars = new HashMap<>();
            vars.put("passwordResetUrl", passwordResetUrl);

            String htmlContent = mailTemplateProcessor.buildTemplate("password-reset.html", vars);

            helper.setTo(user.getEmail());
            helper.setSubject("[Academ-IQ] - 🔒 Reset your password");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Failed to send the reset password email:", ex.getCause());
            throw new MailSendingException("Failed to send the reset password email: " + ex.getMessage());
        }

        log.info(String.format(
                "A recover password e-mail has been sent to '%s' at %s", user.getUsername(), LocalDateTime.now()));
    }

    /**
     * Generate a URL with the given endpoint, origin, and token.
     *
     * @param origin   the origin of the request (e.g. http://localhost:8888)
     * @param endpoint the endpoint to append to the origin (e.g. "verify" or
     *                 "reset-password")
     * @param token    the token to append to the URL
     * @return the generated URL
     */
    private String generateUrl(String origin, String endpoint, String token) {
        return origin + "/" + endpoint + "?token=" + token;
    }

}
