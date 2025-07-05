package dev.luiiscarlos.academ_iq_api.features.learning.enrollment.exception;

import java.io.Serial;
import java.util.UUID;

public class EnrollmentAlreadyExists extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public EnrollmentAlreadyExists(String message) {
        super(message);
    }

    public EnrollmentAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public EnrollmentAlreadyExists(Throwable cause) {
        super(cause);
    }

}
