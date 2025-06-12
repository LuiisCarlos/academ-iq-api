package dev.luiiscarlos.academ_iq_api.course.exception;

import java.io.Serial;
import java.util.UUID;

public class EnrollmentNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public EnrollmentNotFoundException(String message) {
        super(message);
    }

    public EnrollmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnrollmentNotFoundException(Throwable cause) {
        super(cause);
    }

}
