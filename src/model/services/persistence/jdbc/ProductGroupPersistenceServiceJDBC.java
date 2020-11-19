package model.services.persistence.jdbc;

import model.entities.ProductGroup;
import model.services.persistence.abstracts.ProductGroupPersistenceService;

import java.util.List;

public class ProductGroupPersistenceServiceJDBC implements ProductGroupPersistenceService {

    @Override
    public List<ProductGroup> findAll() {
        return null;
    }
}
