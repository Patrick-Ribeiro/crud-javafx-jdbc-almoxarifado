package model.services.persistence.exceptions;

public class DatabaseIntegrityException extends RuntimeException {

    public DatabaseIntegrityException(String message) {
        super(message);
    }
}
