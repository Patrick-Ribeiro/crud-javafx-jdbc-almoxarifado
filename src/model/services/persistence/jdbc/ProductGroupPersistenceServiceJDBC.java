package model.services.persistence.jdbc;

import model.entities.*;
import model.entities.enums.ExpenseType;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
import model.services.persistence.exceptions.DatabaseIntegrityException;
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

public class ProductGroupPersistenceServiceJDBC implements ProductGroupPersistenceService {

    @Override
    public void insert(ProductGroup group) {
        String sql = "INSERT INTO product_groups (id_erp, description, expense_debit) VALUES (?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, group.getId());
            preparedStmt.setString(2, group.getDescription());
            preparedStmt.setInt(3, group.getExpense().getDebit());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM product_groups WHERE id_erp = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            Logs.informationQuery(stmt);
            stmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, stmt);
        } catch (SQLException e) {
            throw new DatabaseIntegrityException(e.getMessage());
        }
    }

    @Override
    public void update(ProductGroup group) {
        String sql = "UPDATE product_groups SET description = ?, expense_debit = ? WHERE id_erp = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, group.getDescription());
            preparedStmt.setInt(2, group.getExpense().getDebit());
            preparedStmt.setInt(3, group.getId());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public ProductGroup find(int id) {
        String sql = "SELECT product_groups.*, expenses.description as expense_description, expenses.type as expense_type "
                + "FROM product_groups "
                + "INNER JOIN expenses ON product_groups.expense_debit = expenses.debit WHERE id_erp = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            ProductGroup groupFound = null;
            if (resultSet.next()) {
                Expense expenseFound = instatiateExpense(resultSet);
                groupFound = instantiateProductGroup(resultSet, expenseFound);
            }

            DatabaseConnection.closeConnection(connection, preparedStmt, resultSet);
            return groupFound;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public List<ProductGroup> findAll() {
        String sql = "SELECT product_groups.*, "
                + "expenses.description as expense_description, "
                + "expenses.type as expense_type "
                + "FROM product_groups "
                + "INNER JOIN expenses ON product_groups.expense_debit = expenses.debit";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            List<ProductGroup> groups = new ArrayList<>();
            Map<Integer, Expense> map = new HashMap<>();
            while (resultSet.next()) {
                Expense expense = map.get(resultSet.getInt("expense_debit"));

                if (expense == null) {
                    expense = instatiateExpense(resultSet);
                    map.put(expense.getDebit(), expense);
                }
                groups.add(instantiateProductGroup(resultSet, expense));
            }
            return groups;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    private ProductGroup instantiateProductGroup(ResultSet resultSet, Expense expense) throws SQLException {
        Integer id = resultSet.getInt("id_erp");
        String description = resultSet.getString("description");
        return new ProductGroup(id, description, expense);
    }

    private Expense instatiateExpense(ResultSet resultSet) throws SQLException {
        Integer expenseDebit = resultSet.getInt("expense_debit");
        String expenseDescription = resultSet.getString("expense_description");
        ExpenseType expenseType = ExpenseType.valueOf(resultSet.getString("expense_type"));
        return new Expense(expenseDebit, expenseDescription, expenseType);
    }


}
