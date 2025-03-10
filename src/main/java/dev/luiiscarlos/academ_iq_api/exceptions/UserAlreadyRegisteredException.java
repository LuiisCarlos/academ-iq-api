package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class UserAlreadyRegisteredException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "User not valid";

    public UserAlreadyRegisteredException() {
        super(MESSAGE);
    }

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }

}
