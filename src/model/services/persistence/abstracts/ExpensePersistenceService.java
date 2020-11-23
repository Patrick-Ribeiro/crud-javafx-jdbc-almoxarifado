package model.services.persistence.abstracts;

import model.entities.Expense;

import java.util.List;

public interface ExpensePersistenceService {

    void insert(Expense expense);

    void delete(int id);

    void update(Expense expense);

    List<Expense> findAll();

    Expense find(int id);
}
