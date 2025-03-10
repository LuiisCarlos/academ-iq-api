package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class StorageNoFileTypeFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "File type not found";

    public StorageNoFileTypeFoundException() {
        super(MESSAGE);
    }

    public StorageNoFileTypeFoundException(String message) {
        super(message);
    }
}
