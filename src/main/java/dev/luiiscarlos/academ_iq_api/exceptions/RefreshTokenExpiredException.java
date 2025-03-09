package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class RefreshTokenExpiredException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Refresh token expired";

    public RefreshTokenExpiredException() {
        super(MESSAGE);
    }

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

}
