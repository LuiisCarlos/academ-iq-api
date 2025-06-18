package dev.luiiscarlos.academ_iq_api.features.identity.auth.exception;

import java.io.Serial;
import java.util.UUID;

public class AuthCredentialsNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public AuthCredentialsNotFoundException(String message) {
        super(message);
    }

    public AuthCredentialsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthCredentialsNotFoundException(Throwable cause) {
        super(cause);
    }

}
