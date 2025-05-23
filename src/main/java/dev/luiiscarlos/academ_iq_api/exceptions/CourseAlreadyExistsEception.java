package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class CourseAlreadyExistsEception extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public CourseAlreadyExistsEception(String message) {
        super(message);
    }

    public CourseAlreadyExistsEception(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseAlreadyExistsEception(Throwable cause) {
        super(cause);
    }

}