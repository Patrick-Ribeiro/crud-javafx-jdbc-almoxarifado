package model.services.persistence.jdbc;

import model.entities.Expense;
import model.entities.Product;
import model.entities.ProductGroup;
import model.entities.UserGroup;
import model.entities.enums.TypeExpense;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
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

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(ProductGroup group) {

    }

    @Override
    public ProductGroup find(int id) {
        return null;
    }

    @Override
    public List<ProductGroup> findAll() {
        String sql = "SELECT product_groups.*, expenses.description as expense_description, expenses.type as expense_type "
                + "FROM product_groups "
                + "INNER JOIN expenses ON product_groups.expense_debit = expenses.debit;";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            List<ProductGroup> groups = new ArrayList<>();
            Map<Integer, Expense> map = new HashMap<>();
            while (resultSet.next()) {
                Expense expense = map.get(resultSet.getInt("expense_debit"));

                if (expense == null) {
                    expense = instatiateExpense(resultSet);
                    map.put(expense.getId(), expense);
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
        TypeExpense typeExpense = TypeExpense.valueOf(resultSet.getString("expense_type"));
        return new Expense(expenseDebit, expenseDescription, typeExpense);
    }


}
