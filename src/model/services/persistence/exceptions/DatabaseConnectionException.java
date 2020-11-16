package model.services.persistence.exceptions;

import util.Logs;

public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message) {
        super(message);
        Logs.error(this);
    }
}
