package model.services.persistence.jdbc;

import model.entities.ProductCategory;
import model.entities.UserGroup;
import model.services.persistence.abstracts.ProductCategoryPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.exceptions.DatabaseIntegrityException;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryPersistenceServiceJDBC implements ProductCategoryPersistenceService {

    @Override
    public void insert(ProductCategory category) throws PersistenceException {
        String sql = "INSERT INTO product_categories (description) VALUE (?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, category.getDescription());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM product_categories WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new DatabaseIntegrityException(ex.getMessage());
        }
    }

    @Override
    public void update(ProductCategory category) {
        String sql = "UPDATE product_categories SET description = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, category.getDescription());
            preparedStmt.setInt(2, category.getId());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public List<ProductCategory> findAll() {
        String sql = "SELECT * FROM product_categories";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            List<ProductCategory> categories = new ArrayList<>();
            while (resultSet.next()) {
                categories.add(fromResultSet(resultSet));
            }
            return categories;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public ProductCategory find(int id) {
        return null;
    }

    private ProductCategory fromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String description = resultSet.getString("description");
        return new ProductCategory(id, description);
    }
}
