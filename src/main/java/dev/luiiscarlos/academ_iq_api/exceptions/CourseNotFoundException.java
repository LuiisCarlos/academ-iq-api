package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class CourseNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Course not found";

    public CourseNotFoundException() {
        super(MESSAGE);
    }

    public CourseNotFoundException(String message) {
        super(message);
    }

}
