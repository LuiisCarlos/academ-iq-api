package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class RatingAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public RatingAlreadyExistsException(String message) {
        super(message);
    }

    public RatingAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RatingAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
