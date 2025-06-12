package dev.luiiscarlos.academ_iq_api.course.exception;

import java.io.Serial;
import java.util.UUID;

public class SectionNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    public SectionNotFoundException(String message) {
        super(message);
    }

    public SectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SectionNotFoundException(Throwable cause) {
        super(cause);
    }

}