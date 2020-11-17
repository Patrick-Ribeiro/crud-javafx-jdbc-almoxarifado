package model.services.persistence.abstracts;

import model.entities.Product;
import model.entities.ProductCategory;
import model.services.persistence.exceptions.PersistenceException;

import java.util.List;

public interface ProductCategoryPersistenceService {

    void insert(ProductCategory category) throws PersistenceException;

    void delete(int id);

    void update(ProductCategory category);

    List<ProductCategory> findAll();

    ProductCategory find(int id);
}
