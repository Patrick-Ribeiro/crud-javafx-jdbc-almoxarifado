package model.services.persistence.abstracts;

import model.entities.Movement;

public interface MovementPersistenceService {

    void register(Movement movement);

    void cancel(int id);

}
