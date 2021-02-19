package model.services.persistence.abstracts;

import model.entities.Order;

import java.util.List;

public interface OrderPersistenceService {

    void insert(Order order);

    void cancel(int id);

    void update(Order order);

    List<Order> findAll();

    Order find(int id);
}
