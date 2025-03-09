package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class InvalidCredentialsException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Invalid username or password";

    public InvalidCredentialsException() {
        super(MESSAGE);
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }

}
