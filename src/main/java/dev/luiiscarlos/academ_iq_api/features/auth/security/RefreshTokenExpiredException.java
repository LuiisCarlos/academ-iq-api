package dev.luiiscarlos.academ_iq_api.features.auth.security;

import java.io.Serial;
import java.util.UUID;

public class RefreshTokenExpiredException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

    public RefreshTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenExpiredException(Throwable cause) {
        super(cause);
    }

}
