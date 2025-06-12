package dev.luiiscarlos.academ_iq_api.review.exception;

import java.io.Serial;
import java.util.UUID;

public class InvalidReviewException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public InvalidReviewException(String message) {
        super(message);
    }

    public InvalidReviewException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReviewException(Throwable cause) {
        super(cause);
    }

}