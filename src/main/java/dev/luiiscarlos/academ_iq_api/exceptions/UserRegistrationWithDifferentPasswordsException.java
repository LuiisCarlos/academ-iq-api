package dev.luiiscarlos.academ_iq_api.exceptions;

import java.util.UUID;

public class UserRegistrationWithDifferentPasswordsException extends BaseException {

    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Passwords do not match";

    public UserRegistrationWithDifferentPasswordsException() {
        super(MESSAGE);
    }

    public UserRegistrationWithDifferentPasswordsException(String message) {
        super(message);
    }

}
