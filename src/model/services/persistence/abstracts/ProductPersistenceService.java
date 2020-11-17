package model.services.persistence.abstracts;

import model.entities.Product;
import model.entities.User;
import model.services.persistence.exceptions.PersistenceException;

import java.util.List;

public interface ProductPersistenceService {

    public void insert(Product product) throws PersistenceException;

    public void delete(int id);

    public void update(Product product);

    public List<Product> findAll();

    public Product find(int id);

    public List<Product> find(String filter);
}
