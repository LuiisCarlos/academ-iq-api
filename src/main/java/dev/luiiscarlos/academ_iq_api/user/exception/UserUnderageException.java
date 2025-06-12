package dev.luiiscarlos.academ_iq_api.user.exception;

import java.io.Serial;
import java.util.UUID;

public class UserUnderageException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public UserUnderageException(String message) {
        super(message);
    }

    public UserUnderageException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserUnderageException(Throwable cause) {
        super(cause);
    }

}
