package model.services.persistence.jdbc;

import model.entities.Order;
import model.entities.OrderItem;
import model.services.persistence.abstracts.OrderPersistenceService;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.*;
import java.util.List;
import java.util.Set;

public class OrderPersistenceServiceJDBC implements OrderPersistenceService {

    @Override
    public void insert(Order order) {
        String sql = "INSERT INTO orders "
                + "(date, user_requester, status)"
                + " VALUE (?,?,?)";

        String sql2 = "INSERT INTO order_products"
                + " (order_id, product_id, product_quantity, product_included)"
                + " VALUE (?,?,?,?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStmt2 = connection.prepareStatement(sql2)) {

            preparedStmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
            preparedStmt.setInt(2, order.getRequester().getCode());
            preparedStmt.setString(3, order.getStatus().name());
            Logs.informationQuery(preparedStmt);
            int rowsAffected = preparedStmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStmt.getGeneratedKeys();
                while (resultSet.next())
                    order.setId(resultSet.getInt(1));

                Set<OrderItem> keySet = order.getItemMap().keySet();
                preparedStmt2.setInt(1, order.getId());
                for (OrderItem item : keySet) {
                    preparedStmt2.setInt(2, item.getProduct().getInternalCode());
                    preparedStmt2.setDouble(3, item.getQuantity());
                    preparedStmt2.setBoolean(4, order.getItemMap().get(item));
                    preparedStmt2.executeUpdate();
                }
            }

            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void cancel(int id) {

    }

    @Override
    public void update(Order order) {

    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order find(int id) {
        return null;
    }
}
