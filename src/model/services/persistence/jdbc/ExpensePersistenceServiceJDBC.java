package model.services.persistence.jdbc;

import model.entities.Expense;
import model.entities.Packing;
import model.entities.enums.ExpenseType;
import model.services.persistence.abstracts.ExpensePersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
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
        String sql = "UPDATE expenses SET description = ?, type = ? WHERE debit = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, expense.getDescription());
            preparedStmt.setString(2, expense.getType().name());
            preparedStmt.setInt(3, expense.getDebit());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new DatabaseConnectionException(ex.getMessage());
        }
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
        String sql = "SELECT * FROM expenses WHERE debit = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();
            resultSet.next();

            Expense expenseFound = instantiateEntity(resultSet);
            DatabaseConnection.closeConnection(connection, preparedStmt, resultSet);
            return expenseFound;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    private Expense instantiateEntity(ResultSet resultSet) throws SQLException {
        String description = resultSet.getString("description");
        Integer debit = resultSet.getInt("debit");
        ExpenseType type = ExpenseType.valueOf(resultSet.getString("type"));

        return new Expense(debit, description, type);
    }
}
