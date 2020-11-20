package model.services.persistence.abstracts;

import model.entities.ProductGroup;

import java.util.List;

public interface ProductGroupPersistenceService {

    void insert(ProductGroup group);

    void delete(int id);

    void update(ProductGroup group);

    ProductGroup find(int id);

    List<ProductGroup> findAll();
}
