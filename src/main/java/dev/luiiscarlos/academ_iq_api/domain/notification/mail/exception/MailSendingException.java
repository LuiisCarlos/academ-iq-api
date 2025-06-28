package dev.luiiscarlos.academ_iq_api.domain.notification.mail.exception;

import java.io.Serial;
import java.util.UUID;

public class MailSendingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public MailSendingException(String message) {
        super(message);
    }

    public MailSendingException(Throwable cause) {
        super(cause);
    }

    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailSendingException(String messageFormat, Object... args) {
        super(String.format(messageFormat, args));
    }

    public MailSendingException(String messageFormat, Throwable cause, Object... args) {
        super(String.format(messageFormat, args), cause);
    }

}
