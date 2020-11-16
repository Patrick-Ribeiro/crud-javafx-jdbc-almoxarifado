package model.services.persistence.jdbc;

import model.entities.Packing;
import model.services.persistence.abstracts.PackingPersistenceService;
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

public class PackingPersistenceServiceJBDC implements PackingPersistenceService {

    @Override
    public void insert(Packing packing) {
        String sql = "INSERT INTO packings (description, abbreviation) VALUES (?,?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, packing.getDescription());
            preparedStmt.setString(2, packing.getAbbreviation());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM packings WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new DatabaseIntegrityException(ex.getMessage());
        }
    }

    @Override
    public void update(Packing packing) {
        String sql = "UPDATE packings SET description = ?, abbreviation = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, packing.getDescription());
            preparedStmt.setString(2, packing.getAbbreviation());
            preparedStmt.setInt(3, packing.getId());
            Logs.informationQuery(preparedStmt);
            preparedStmt.executeUpdate();

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new DatabaseConnectionException(ex.getMessage());
        }
    }

    @Override
    public List<Packing> findAll() {
        String sql = "SELECT * FROM packings";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();

            List<Packing> packings = new ArrayList<>();
            while (resultSet.next()) {
                packings.add(fromResultSet(resultSet));
            }
            return packings;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public Packing find(int id) {
        String sql = "SELECT * FROM packings WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, id);
            Logs.informationQuery(preparedStmt);
            ResultSet resultSet = preparedStmt.executeQuery();
            resultSet.next();

            Packing packingFound = fromResultSet(resultSet);
            DatabaseConnection.closeConnection(connection, preparedStmt, resultSet);
            return packingFound;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    private Packing fromResultSet(ResultSet resultSet) throws SQLException {
        Integer id = resultSet.getInt("id");
        String description = resultSet.getString("description");
        String abbreviation = resultSet.getString("abbreviation");
        return new Packing(id, description, abbreviation);
    }
}
