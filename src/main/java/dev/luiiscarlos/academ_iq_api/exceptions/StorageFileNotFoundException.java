package dev.luiiscarlos.academ_iq_api.exceptions;

import java.io.Serial;
import java.util.UUID;

public class StorageFileNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = UUID.randomUUID().getMostSignificantBits();

    private static final String MESSAGE = "Storage file not found";

    public StorageFileNotFoundException() {
        super(MESSAGE);
    }

    public StorageFileNotFoundException(String message) {
        super(message);
    }

}
