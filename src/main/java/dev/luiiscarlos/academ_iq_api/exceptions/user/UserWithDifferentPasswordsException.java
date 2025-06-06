package dev.luiiscarlos.academ_iq_api.exceptions.user;

import java.io.Serial;
import java.util.UUID;

public class UserWithDifferentPasswordsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public UserWithDifferentPasswordsException(String message) {
        super(message);
    }

    public UserWithDifferentPasswordsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserWithDifferentPasswordsException(Throwable cause) {
        super(cause);
    }

}
