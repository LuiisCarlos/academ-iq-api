package dev.luiiscarlos.academ_iq_api.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import dev.luiiscarlos.academ_iq_api.exceptions.EmailSendingException;
import dev.luiiscarlos.academ_iq_api.models.User;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final TokenServiceImpl tokenService;

    private final JavaMailSender mailSender;

    private final Dotenv dotenv;

    public void sendConfirmationEmail(User user) {
        try {
            //MIME - HTML message
            String token = generateConfirmationLink(tokenService.generateConfirmationToken(user));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String htmlContent = String.format("<html>" +
                "<head>" +
                    "<style>" +
                        "body {" +
                            "font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;" +
                            "background-color: #f4f4f4;" +
                            "color: #444;" +
                            "margin: 0;" +
                            "padding: 0;" +
                        "}" +
                        ".container {" +
                            "max-width: 600px;" +
                            "margin: 20px auto;" +
                            "background: #ffffff;" +
                            "padding: 20px;" +
                            "text-align: left;" +
                            "font-size: 14px;" +
                            "border-radius: 8px;" +
                            "box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);" +
                        "}" +
                        ".logo-container, form {" +
                            "text-align: center;" +
                        "}" +
                        ".logo {" +
                            "padding-top: 10px;" +
                            "width: 160px;" +
                            "height: 160px;" +
                        "}" +
                        ".button {" +
                            "display: inline-block;" +
                            "background-color: #007bff;" +
                            "color: #ffffff;" +
                            "padding: 16px 26px;" +
                            "text-decoration: none;" +
                            "border-radius: 5px;" +
                            "margin-top: 40px;" +
                            "margin-bottom: 40px;" +
                            "font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;" +
                            "font-size: 15px;" +
                            "font-weight: bold;" +
                        "}" +
                        ".footer {" +
                            "margin-top: 30px;" +
                            "font-size: 14px;" +
                            "color: #777;" +
                        "}" +
                        ".footer p {" +
                            "font-size: 11px;" +
                            "margin: 10px 0 ;" +
                        "}" +
                        ".link-text {" +
                            "word-break: break-word;" +
                        "}" +
                        ".separator {" +
                            "margin: 20px 0;" +
                            "border-top: 1px solid #eee;" +
                        "}" +
                    "</style>" +
                "</head>" +
                "<body>" +
                    "<div class='container'>" +
                        "<div class='logo-container'>" +
                            "<img src='http://localhost:8888/api/v1/files/academiq-logo.png' alt='Academ-IQ Logo' class='logo'>" +
                        "</div>" +
                        "<p style='text-align: center; font-size: 52px; margin-top: 40px; margin-bottom: 0px; font-weight: bold;'>Verify your account</p>" +
                        "<div class='separator'></div>" +
                        "<p>Thank you for signing up at Academ-IQ!</p>" +
                        "<p>To complete your registration, please click the button below to verify your email address:</p>" +
                        "<div style='text-align: center;'>" +
                            "<a href='%s' class='button' style='color: #fff;'>VERIFY ACCOUNT</a>" +
                        "</div>" +
                        "<p>If the button does not work, you can access it through the following link:</p>" +
                        "<p class='link-text' style='color: #666; font-size: 13px;'>" +
                            "<a href='%s' target='_blank'>%s</a>" +
                        "</p>" +
                        "<p style='color: #555; font-size: 12px; font-style: italic;'>This link will expire in 24 hours.</p>" +
                        "<div class='separator'></div>" +
                        "<div class='footer'>" +
                            "<p>If you did not request this, you can ignore this message.</p>" +
                            "<a href='https://github.com/LuiisCarlos' target='_blank'>" +
                                "<img src='https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png' alt='GitHub Logo' class='github-logo' width='32px' height='32px'>" +
                            "</a>" +
                            "<p>&copy; 2025 Academ-IQ. All rights reserved.</p>" +
                        "</div>" +
                    "</div>" +
                "</body>" +
            "</html>", token, token, token);

            helper.setTo(user.getEmail());
            helper.setSubject("[Academ-IQ] - 🔒 Verify your account");
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new EmailSendingException("Failed to send the confirmation email: " + ex.getMessage());
        }
    }

    public String generateConfirmationLink(String token) {
        String address = dotenv.get("SERVER_PROTOCOL") + "://" +  dotenv.get("SERVER_DOMAIN") + ":" +  dotenv.get("SERVER_PORT");
        return address + "/api/v1/auth/verify?token=" + token;
    }

}
