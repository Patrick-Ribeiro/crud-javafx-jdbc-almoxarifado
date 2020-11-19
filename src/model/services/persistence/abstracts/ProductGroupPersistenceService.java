package model.services.persistence.abstracts;

import model.entities.ProductGroup;

import java.util.List;

public interface ProductGroupPersistenceService {

    List<ProductGroup> findAll();
}
