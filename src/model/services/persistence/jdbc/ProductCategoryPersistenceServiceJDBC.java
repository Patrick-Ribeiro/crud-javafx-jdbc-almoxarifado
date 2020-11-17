package model.services.persistence.jdbc;

import model.entities.ProductCategory;
import model.services.persistence.abstracts.ProductCategoryPersistenceService;
import model.services.persistence.exceptions.PersistenceException;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryPersistenceServiceJDBC implements ProductCategoryPersistenceService {

    @Override
    public void insert(ProductCategory category) throws PersistenceException {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(ProductCategory category) {

    }

    @Override
    public List<ProductCategory> findAll() {
        return new ArrayList<>();
    }

    @Override
    public ProductCategory find(int id) {
        return null;
    }
}
