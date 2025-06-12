package dev.luiiscarlos.academ_iq_api.features.review.exception;

import java.io.Serial;
import java.util.UUID;

public class ReviewAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public ReviewAlreadyExistsException(String message) {
        super(message);
    }

    public ReviewAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}
