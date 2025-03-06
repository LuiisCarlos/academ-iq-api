package dev.luiiscarlos.academ_iq_api.exceptions;

import java.util.UUID;

public class RoleNotFoundException extends BaseException {

    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Role not found";

    public RoleNotFoundException() {
        super(MESSAGE);
    }

    public RoleNotFoundException(String message) {
        super(message);
    }

}
