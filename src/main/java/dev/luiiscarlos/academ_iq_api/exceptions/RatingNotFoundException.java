package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class RatingNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public RatingNotFoundException(String message) {
        super(message);
    }

    public RatingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RatingNotFoundException(Throwable cause) {
        super(cause);
    }

}
