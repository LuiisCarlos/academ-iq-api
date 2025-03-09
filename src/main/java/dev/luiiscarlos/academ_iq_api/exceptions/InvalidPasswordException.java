package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class InvalidPasswordException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Invalid password";

    public InvalidPasswordException() {
        super(MESSAGE);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

}
