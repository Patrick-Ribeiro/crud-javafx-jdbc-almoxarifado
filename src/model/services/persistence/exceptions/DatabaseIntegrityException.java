package model.services.persistence.exceptions;

import util.Logs;

public class DatabaseIntegrityException extends RuntimeException {

    public DatabaseIntegrityException(String message) {
        super(message);
        Logs.error(this);
    }
}
