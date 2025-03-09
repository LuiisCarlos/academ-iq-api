package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class UserIsAlreadyRegisteredException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "User not valid";

    public UserIsAlreadyRegisteredException() {
        super(MESSAGE);
    }

    public UserIsAlreadyRegisteredException(String message) {
        super(message);
    }

}
