package model.services.persistence.jdbc;

import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserGroupPersistenceService;
import model.services.persistence.abstracts.UserPersistenceService;
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
        String sql = "INSERT INTO usuario"
                + " (code, name, email, telephone, group_id, active)"
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
            ex.printStackTrace();
            Logs.error(ex);
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(User user) {

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
            Logs.error(e);
            return new ArrayList<>();
        }
    }

    @Override
    public User find(int id) {
        return null;
    }

    private User fromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("code_erp");
        String name = resultSet.getString("name");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        Integer idGroup = resultSet.getInt("user_group");
        UserGroup userGroup = new UserGroupPersistenceServiceJDBC().find(idGroup);
        boolean active = resultSet.getBoolean("active");

        User user = new User(id, name, userGroup, active);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }
}

