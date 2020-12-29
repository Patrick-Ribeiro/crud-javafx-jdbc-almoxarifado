package model.services.persistence.jdbc;

import model.entities.*;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.*;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPersistenceServiceJDBC implements ProductPersistenceService {

    private ProductCategoryPersistenceService serviceCategory = PersistenceServiceFactory.createProductCategoryService();
    private ProductGroupPersistenceService serviceGroup =  PersistenceServiceFactory.createProductGroupService();
    private PackingPersistenceService servicePacking =  PersistenceServiceFactory.createPackingService();
    private UserPersistenceService serviceBuyer =  PersistenceServiceFactory.createUserService();

    @Override
    public void insert(Product product) throws PersistenceException {
        if (find(product.getInternalCode()) != null) {
            throw new PersistenceException("O produto " + product.getInternalCode() + " já existe");
        }
        String sql = "INSERT INTO products (internal_code, description, description_erp," +
                " category_id, group_id, packing_id, quantity_packing, buyer_user_id, active) VALUES" +
                "(?,?,?,?,?,?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, product.getInternalCode());
            preparedStmt.setString(2, product.getDescription());
            preparedStmt.setString(3, product.getDescriptionERP());
            preparedStmt.setInt(4, product.getCategory().getId());
            preparedStmt.setInt(5, product.getGroup().getId());
            preparedStmt.setInt(6, product.getPacking().getId());
            preparedStmt.setInt(7, product.getQuantityPacking());
            preparedStmt.setInt(8, product.getBuyer().getCode());
            preparedStmt.setBoolean(9, product.isActive());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET "
                + "description = ?, description_erp = ?, "
                + "category_id = ?, group_id = ?, "
                + "packing_id = ?, quantity_packing = ?, "
                + "buyer_user_id = ?, active = ? "
                + "WHERE internal_code = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, product.getDescription());
            preparedStmt.setString(2, product.getDescriptionERP());
            preparedStmt.setInt(3, product.getCategory().getId());
            preparedStmt.setInt(4, product.getGroup().getId());
            preparedStmt.setInt(5, product.getPacking().getId());
            preparedStmt.setInt(6, product.getQuantityPacking());
            preparedStmt.setInt(7, product.getBuyer().getCode());
            preparedStmt.setBoolean(8, product.isActive());
            preparedStmt.setInt(9, product.getInternalCode());

            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();
            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(stmt);
            ResultSet resultSet = stmt.executeQuery();

            List<Product> list = new ArrayList<>();
            Map<Integer, ProductCategory> mapCategory = new HashMap<>();
            Map<Integer, ProductGroup> mapGroup = new HashMap<>();
            Map<Integer, Packing> mapPacking = new HashMap<>();
            Map<Integer, User> mapBuyer = new HashMap<>();
            while (resultSet.next()) {
                Integer categoryID = resultSet.getInt("category_id");
                Integer groupID = resultSet.getInt("group_id");
                Integer packingID = resultSet.getInt("packing_id");
                Integer buyerID = resultSet.getInt("buyer_user_id");

                ProductCategory category = mapCategory.get(categoryID);
                ProductGroup group = mapGroup.get(groupID);
                Packing packing = mapPacking.get(packingID);
                User buyer = mapBuyer.get(buyerID);

                if (category == null) {
                    category = instantiateCategory(categoryID);
                    mapCategory.put(category.getId(), category);
                }
                if (group == null) {
                    group = instantiateGroup(groupID);
                    mapGroup.put(group.getId(), group);
                }
                if (packing == null) {
                    packing = instantiatePacking(packingID);
                    mapPacking.put(packing.getId(), packing);
                }
                if (buyer == null) {
                    buyer = instantiateBuyer(buyerID);
                    mapBuyer.put(buyer.getCode(), buyer);
                }
                list.add(instantiateEntity(resultSet, category, group, packing, buyer));
            }
            DatabaseConnection.closeConnection(connection, stmt, resultSet);
            return list;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public Product find(int id) {
        return null;
    }

    @Override
    public List<Product> find(String filter) {
        return null;
    }

    private ProductCategory instantiateCategory(Integer id) {
        return serviceCategory.find(id);
    }

    private ProductGroup instantiateGroup(Integer id) {
        return serviceGroup.find(id);
    }

    private Packing instantiatePacking(Integer id) {
        return servicePacking.find(id);
    }

    private User instantiateBuyer(Integer id) {
        return serviceBuyer.find(id);
    }

    private Product instantiateEntity(ResultSet resultSet, ProductCategory category, ProductGroup group, Packing packing, User buyer) throws SQLException {
        Integer internalCode = resultSet.getInt("internal_code");
        String description = resultSet.getString("description");
        String descriptionERP = resultSet.getString("description_erp");
        Integer quantityPacking = resultSet.getInt("quantity_packing");
        Boolean active = resultSet.getBoolean("active");

        Product product = new Product();
        product.setInternalCode(internalCode);
        product.setDescription(description);
        product.setDescriptionERP(descriptionERP);
        product.setCategory(category);
        product.setGroup(group);
        product.setPacking(packing);
        product.setQuantityPacking(quantityPacking);
        product.setBuyer(buyer);
        product.setActive(active);
        return product;
    }
}
