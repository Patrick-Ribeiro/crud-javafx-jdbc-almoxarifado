package model.services.persistence.jdbc;

import model.entities.Expense;
import model.entities.Packing;
import model.entities.enums.TypeExpense;
import model.services.persistence.abstracts.ExpensePersistenceService;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpensePersistenceServiceJDBC implements ExpensePersistenceService {

    @Override
    public void insert(Expense expense) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(Expense expense) {

    }

    @Override
    public List<Expense> findAll() {
        String sql = "SELECT * FROM expenses ORDER BY debit";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            List<Expense> expenses = new ArrayList<>();
            while (resultSet.next()) {
                expenses.add(instantiateEntity(resultSet));
            }
            return expenses;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public Expense find(int id) {
        return null;
    }

    private Expense instantiateEntity(ResultSet resultSet) throws SQLException {
        String description = resultSet.getString("description");
        Integer debit = resultSet.getInt("debit");
        TypeExpense type = TypeExpense.valueOf(resultSet.getString("type"));

        return new Expense(debit, description, type);
    }
}
