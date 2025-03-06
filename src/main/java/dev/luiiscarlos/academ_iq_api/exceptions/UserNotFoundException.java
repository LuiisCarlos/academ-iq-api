package dev.luiiscarlos.academ_iq_api.exceptions;

import java.util.UUID;

public class UserNotFoundException extends BaseException {

    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "User not found";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

}
