package model.services.persistence.jdbc;

import model.entities.UserGroup;
import model.services.persistence.abstracts.UserGroupPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGroupPersistenceServiceJDBC implements UserGroupPersistenceService {

    @Override
    public void insert(UserGroup userGroup) {
        String sql = "INSERT INTO user_groups (description) VALUE (?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, userGroup.getDescription());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            Logs.error(ex);
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(UserGroup userGroup) {
        if (userGroup.getId() == null) {
            throw new PersistenceException("O grupo de usuário " + userGroup.getDescription() + " não existe.");
        }
        String sql = "UPDATE user_groups SET description = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, userGroup.getDescription());
            preparedStmt.setInt(2, userGroup.getId());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            Logs.error(ex);
            throw new DatabaseConnectionException(ex.getMessage());
        }
    }

    @Override
    public List<UserGroup> findAll() {
        String sql = "SELECT * FROM user_groups";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            List<UserGroup> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(fromResultSet(resultSet));
            }
            return groups;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public UserGroup find(int id) {
        return null;
    }

    private UserGroup fromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String description = resultSet.getString("description");
        UserGroup userGroup = new UserGroup(id, description);
        System.out.println(userGroup.getDescription());
        return userGroup;
    }
}
