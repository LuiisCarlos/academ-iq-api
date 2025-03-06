package dev.luiiscarlos.academ_iq_api.exceptions;

import java.util.UUID;

public class UserIsAlreadyRegisteredException extends BaseException {

    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "The user is not valid";

    public UserIsAlreadyRegisteredException() {
        super(MESSAGE);
    }

    public UserIsAlreadyRegisteredException(String message) {
        super(message);
    }

}
