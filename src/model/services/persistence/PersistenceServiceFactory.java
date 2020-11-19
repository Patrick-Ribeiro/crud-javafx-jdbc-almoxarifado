package model.services.persistence;

import model.services.persistence.abstracts.*;
import model.services.persistence.jdbc.*;

public class PersistenceServiceFactory {

    public static PackingPersistenceService createPackingService() {
        return new PackingPersistenceServiceJBDC();
    }

    public static ProductPersistenceService createProductService() {
        return new ProductPersistenceServiceJDBC();
    }

    public static ProductCategoryPersistenceService createProductCategoryService() {
        return new ProductCategoryPersistenceServiceJDBC();
    }

    public static ProductGroupPersistenceService createProductGroupService() {
        return new ProductGroupPersistenceServiceJDBC();
    }

    public static UserGroupPersistenceService createUserGroupService() {
        return new UserGroupPersistenceServiceJDBC();
    }

}
