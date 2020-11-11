package model.services.persistence.jdbc;

import model.entities.User;
import model.services.persistence.exceptions.PersistenceException;
import model.services.persistence.abstracts.UserPersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import util.Logs;

public class UserPersistenceJDBC implements UserPersistence {

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
        return null;
    }

    @Override
    public User find(int id) {
        return null;
    }
}

