package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class RefreshTokenNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Refresh token not found";

    public RefreshTokenNotFoundException() {
        super(MESSAGE);
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

}
