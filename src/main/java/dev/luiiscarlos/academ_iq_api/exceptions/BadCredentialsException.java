package dev.luiiscarlos.academ_iq_api.exceptions;

import java.util.UUID;

public class BadCredentialsException extends BaseException {

    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Invalid username or password";

    public BadCredentialsException() {
        super(MESSAGE);
    }

    public BadCredentialsException(String message) {
        super(message);
    }

}
