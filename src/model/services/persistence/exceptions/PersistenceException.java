package model.services.persistence.exceptions;

import util.Logs;

public class PersistenceException extends RuntimeException {

    public PersistenceException(String message) {
        super(message);
        Logs.error(this);
    }
}
