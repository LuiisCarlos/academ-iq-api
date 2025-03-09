package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class StorageException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Storage exception";

    public StorageException() {
        super(MESSAGE);
    }

    public StorageException(String message) {
        super(message);
    }

}
