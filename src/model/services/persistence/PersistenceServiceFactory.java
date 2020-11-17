package model.services.persistence;

import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.jdbc.ProductPersistenceServiceJDBC;

public class PersistenceServiceFactory {

    public static ProductPersistenceService createProductPersistenceService () {
        return new ProductPersistenceServiceJDBC();
    }
}
