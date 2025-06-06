package dev.luiiscarlos.academ_iq_api.exceptions.course;

import java.io.Serial;
import java.util.UUID;

public class CourseAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public CourseAlreadyExistsException(String message) {
        super(message);
    }

    public CourseAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseAlreadyExistsException(Throwable cause) {
        super(cause);
    }

}