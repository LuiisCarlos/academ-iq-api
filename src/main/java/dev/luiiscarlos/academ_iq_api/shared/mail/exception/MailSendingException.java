package dev.luiiscarlos.academ_iq_api.shared.mail.exception;

import java.io.Serial;
import java.util.UUID;

public class MailSendingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public MailSendingException(String message) {
        super(message);
    }

    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailSendingException(Throwable cause) {
        super(cause);
    }

}
