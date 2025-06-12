package dev.luiiscarlos.academ_iq_api.auth.security;

import java.io.Serial;
import java.util.UUID;

public class InvalidTokenTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public InvalidTokenTypeException(String message) {
        super(message);
    }

    public InvalidTokenTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenTypeException(Throwable cause) {
        super(cause);
    }

}
