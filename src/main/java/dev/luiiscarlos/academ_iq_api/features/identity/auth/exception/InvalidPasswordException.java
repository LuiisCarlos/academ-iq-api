package dev.luiiscarlos.academ_iq_api.features.identity.auth.exception;

import java.io.Serial;
import java.util.UUID;

public class InvalidPasswordException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPasswordException(Throwable cause) {
        super(cause);
    }

}
