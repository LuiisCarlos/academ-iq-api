package dev.luiiscarlos.academ_iq_api.features.user.exception;

import java.io.Serial;
import java.util.UUID;

public class UserAccountNotVerifiedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public UserAccountNotVerifiedException(String message) {
        super(message);
    }

    public UserAccountNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAccountNotVerifiedException(Throwable cause) {
        super(cause);
    }

}
