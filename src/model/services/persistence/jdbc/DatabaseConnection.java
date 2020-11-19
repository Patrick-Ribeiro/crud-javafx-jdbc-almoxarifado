package model.services.persistence.jdbc;

import model.services.persistence.exceptions.DatabaseConnectionException;
import util.Logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            Properties properties = loadProperties();
            String url = properties.getProperty("dburl");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println();
            } catch (SQLException ex) {
                throw new DatabaseConnectionException(ex.getMessage());
            }
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement preparedStatement) {
        closeConnection(connection);
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                throw new DatabaseConnectionException(ex.getMessage());
            }
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        closeConnection(connection, preparedStatement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                throw new DatabaseConnectionException(ex.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream(new File("").getCanonicalPath()
                + "/settings/database.properties")) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            Logs.error(e);
            throw new DatabaseConnectionException("Arquivo de configurações não encontrado.");
        }
    }
}
