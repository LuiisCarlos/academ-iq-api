package dev.luiiscarlos.academ_iq_api.exceptions.misc;

import java.io.Serial;
import java.util.UUID;

public class EmailSendingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public EmailSendingException(String message) {
        super(message);
    }

    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailSendingException(Throwable cause) {
        super(cause);
    }

}
