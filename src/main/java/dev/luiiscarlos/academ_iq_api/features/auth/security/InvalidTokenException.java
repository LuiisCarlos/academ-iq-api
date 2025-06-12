package dev.luiiscarlos.academ_iq_api.features.auth.security;

import java.io.Serial;
import java.util.UUID;

public class InvalidTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(Throwable cause) {
        super(cause);
    }

}
