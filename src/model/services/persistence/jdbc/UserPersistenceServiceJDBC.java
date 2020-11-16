package model.services.persistence.jdbc;

import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserGroupPersistenceService;
import model.services.persistence.abstracts.UserPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.exceptions.DatabaseIntegrityException;
import model.services.persistence.exceptions.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.Logs;

public class UserPersistenceServiceJDBC implements UserPersistenceService {

    @Override
    public void insert(User user) {
        if (find(user.getCode()) != null) {
            throw new PersistenceException("O usuário de código " + user.getCode() + " já está cadastrado.");
        }
        String sql = "INSERT INTO users"
                + " (code_erp, name, email, telephone, user_group, active)"
                + " VALUE (?,?,?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, user.getCode());
            preparedStmt.setString(2, user.getName());
            preparedStmt.setString(3, user.getEmail());
            preparedStmt.setString(4, user.getTelephone());
            preparedStmt.setInt(5, user.getGroup().getId());
            preparedStmt.setBoolean(6, user.isActive());

            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void delete(int code) {
        String sql = "DELETE FROM users WHERE code_erp = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, code);
            Logs.informationQuery(stmt);
            stmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, stmt);
        } catch (SQLException e) {
            throw new DatabaseIntegrityException(e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET "
                + "name = ?, email = ?, telephone = ?, user_group = ?, active = ?"
                + " WHERE code_erp = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, user.getName());
            preparedStmt.setString(2, user.getEmail());
            preparedStmt.setString(3, user.getTelephone());
            preparedStmt.setInt(4, user.getGroup().getId());
            preparedStmt.setBoolean(5, user.isActive());
            preparedStmt.setInt(6, user.getCode());

            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(stmt);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                users.add(fromResultSet(resultSet));
            }
            DatabaseConnection.closeConnection(connection, stmt, resultSet);
            return users;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public User find(int code) {
        String sql = "SELECT * FROM users WHERE code_erp = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(stmt);
            stmt.setInt(1, code);
            ResultSet resultSet = stmt.executeQuery();

            User userFound;
            if (resultSet.next())
                userFound = fromResultSet(resultSet);
            else
                userFound = null;

            DatabaseConnection.closeConnection(connection, stmt, resultSet);
            return userFound;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public List<User> find(String filter) {
        String sql = "SELECT * FROM users WHERE name LIKE ? OR code_erp LIKE ? OR email LIKE ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(stmt);
            stmt.setString(1, filter + "%");
            stmt.setString(2, filter + "%");
            stmt.setString(3, filter + "%");
            ResultSet resultSet = stmt.executeQuery();

            List<User> usersFound = new ArrayList<>();
            while (resultSet.next())
                usersFound.add(fromResultSet(resultSet));

            DatabaseConnection.closeConnection(connection, stmt, resultSet);
            return usersFound;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    private User fromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("code_erp");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        String telephone = resultSet.getString("telephone");
        Integer idGroup = resultSet.getInt("user_group");
        UserGroup userGroup = new UserGroupPersistenceServiceJDBC().find(idGroup);
        boolean active = resultSet.getBoolean("active");

        User user = new User(id, name, userGroup, active);
        user.setPassword(password);
        user.setEmail(email);
        user.setTelephone(telephone);
        return user;
    }
}

