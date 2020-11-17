package model.services.persistence;

import model.services.persistence.abstracts.PackingPersistenceService;
import model.services.persistence.abstracts.ProductCategoryPersistenceService;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.jdbc.PackingPersistenceServiceJBDC;
import model.services.persistence.jdbc.ProductCategoryPersistenceServiceJDBC;
import model.services.persistence.jdbc.ProductGroupPersistenceServiceJDBC;
import model.services.persistence.jdbc.ProductPersistenceServiceJDBC;

public class PersistenceServiceFactory {

    public static PackingPersistenceService createPackingPersistenceService() {
        return new PackingPersistenceServiceJBDC();
    }

    public static ProductPersistenceService createProductPersistenceService() {
        return new ProductPersistenceServiceJDBC();
    }

    public static ProductCategoryPersistenceService createProductCategoryPersistenceService() {
        return new ProductCategoryPersistenceServiceJDBC();
    }

    public static ProductGroupPersistenceService createProductGroupPersistenceService() {
        return new ProductGroupPersistenceServiceJDBC();
    }

}
