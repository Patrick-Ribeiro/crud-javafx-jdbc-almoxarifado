package model.services.persistence.jdbc;

import model.entities.Product;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductPersistenceServiceJDBC implements ProductPersistenceService {

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
