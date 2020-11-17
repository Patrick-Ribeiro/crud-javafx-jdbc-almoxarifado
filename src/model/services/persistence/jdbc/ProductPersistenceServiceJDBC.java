package model.services.persistence.jdbc;

import model.entities.Product;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.exceptions.PersistenceException;

import java.util.List;

public class ProductPersistenceServiceJDBC implements ProductPersistenceService {

    @Override
    public void insert(Product product) throws PersistenceException {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Product product) {

    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public Product find(int id) {
        return null;
    }

    @Override
    public List<Product> find(String filter) {
        return null;
    }
}
