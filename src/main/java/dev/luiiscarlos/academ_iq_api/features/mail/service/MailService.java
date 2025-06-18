package dev.luiiscarlos.academ_iq_api.features.mail.service;

import dev.luiiscarlos.academ_iq_api.features.identity.user.model.User;
import dev.luiiscarlos.academ_iq_api.features.mail.exception.MailSendingException;

public interface MailService {

    /**
     * Send an email to the user with a link to verify their email address.
     * The link will contain a token that will be used to verify the user's
     * identity.
     *
     * @param user   the user to send the email to
     * @param origin the origin of the request (e.g. http://localhost:8888)
     * @throws MailSendingException if there is an error sending the email
     */
    void sendVerificationMail(User user, String origin);

    /**
     * Send an email to the user with a link to reset their password.
     * The link will contain a token that will be used to verify the user's
     * identity.
     *
     * @param user   the user to send the email to
     * @param origin the origin of the request (e.g. http://localhost:8080)
     * @throws MailSendingException if there is an error sending the email
     */
    void sendResetPasswordMail(User user, String origin);

}
