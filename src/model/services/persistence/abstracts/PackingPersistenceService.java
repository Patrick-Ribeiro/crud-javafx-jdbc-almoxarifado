package model.services.persistence.abstracts;

import model.entities.Packing;
import model.entities.UserGroup;

import java.util.List;

public interface PackingPersistenceService {

    void insert(Packing packing);

    void delete(int id);

    void update(Packing packing);

    List<Packing> findAll();

    Packing find(int id);
}
