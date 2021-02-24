package model.services.persistence.jdbc;

import com.sun.org.apache.xpath.internal.operations.Or;
import model.entities.Order;
import model.entities.OrderItem;
import model.entities.Product;
import model.entities.User;
import model.entities.enums.OrderStatus;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.OrderPersistenceService;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.exceptions.PersistenceException;
import util.Logs;

import java.sql.*;
import java.util.*;

public class OrderPersistenceServiceJDBC implements OrderPersistenceService {

    @Override
    public void insert(Order order) {
        String sql = "INSERT INTO orders "
                + "(date, user_requester, status)"
                + " VALUES (?,?,?)";

        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement preparedStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            connection.setAutoCommit(false);
            preparedStmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
            preparedStmt.setInt(2, order.getRequester().getCode());
            preparedStmt.setString(3, order.getStatus().name());

            Logs.informationQuery(preparedStmt);
            int rowsAffected = preparedStmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet resultSet = preparedStmt.getGeneratedKeys();
                while (resultSet.next())
                    order.setId(resultSet.getInt(1));

                insertOrderItems(order, connection);
                connection.commit();
            } else {
                connection.rollback();
                throw new PersistenceException("Transação revertida por um erro ao gravar o pedido.");
            }
            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            PersistenceException exception = null;
            try {
                connection.rollback();
                exception = new PersistenceException("Transação revertida por um erro ao gravar os itens do pedido. Causado por: " + ex.getMessage());
            } catch (SQLException throwables) {
                exception = new PersistenceException("Erro ao reverter transação causado por: " + ex.getMessage());
            } finally {
                DatabaseConnection.closeConnection(connection);
                if (exception != null)
                    throw exception;
            }
        }
    }

    @Override
    public void cancel(int id) {
        String sql = "UPDATE orders" +
                " status = ?" +
                " WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setString(1, OrderStatus.CANCELED.name());
            preparedStmt.setInt(2, id);

            preparedStmt.executeUpdate();
            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE orders" +
                " SET date = ?, user_requester = ?, status = ?" +
                " WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setTimestamp(1, new Timestamp(order.getDate().getTime()));
            preparedStmt.setInt(2, order.getRequester().getCode());
            preparedStmt.setString(3, order.getStatus().name());
            preparedStmt.setInt(4, order.getId());

            preparedStmt.executeUpdate();
            DatabaseConnection.closeConnection(connection, preparedStmt);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT orders.*, items.product_id, items.product_quantity, items.product_included"
                + " FROM orders"
                + " JOIN order_products items"
                + " ON orders.id = items.order_id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStmt = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStmt.executeQuery();
            List<Order> ordersList = new ArrayList<>();
            while (resultSet.next())
                ordersList.add(instantiateEntity(resultSet));
            return ordersList;
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public Order find(int id) {
        String sql = "SELECT orders.*, items.product_id, items.product_quantity, items.product_included"
                + " FROM orders"
                + " JOIN order_products items"
                + " ON orders.id = items.order_id"
                + " WHERE orders.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStmt = connection.prepareStatement(sql)) {
            preparedStmt.setInt(1, id);

            ResultSet resultSet = preparedStmt.executeQuery();
            resultSet.next();
            return instantiateEntity(resultSet);
        } catch (SQLException ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    private void insertOrderItems(Order order, Connection connection) throws SQLException {
        String sql = "INSERT INTO order_products"
                + " (order_id, product_id, product_quantity, product_included)"
                + " VALUES (?,?,?,?)";

        PreparedStatement preparedStmt = connection.prepareStatement(sql);
        Set<OrderItem> keySet = order.getItemMap().keySet();

        preparedStmt.setInt(1, order.getId());
        for (OrderItem item : keySet) {
            preparedStmt.setInt(2, item.getProduct().getInternalCode());
            preparedStmt.setDouble(3, item.getQuantity());
            preparedStmt.setBoolean(4, order.getItemMap().get(item));
            preparedStmt.executeUpdate();
        }
    }

    private Order instantiateEntity(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setDate(resultSet.getDate("date"));
        User requester = PersistenceServiceFactory.createUserService().find(resultSet.getInt("user_requester"));
        order.setRequester(requester);
        order.setStatus(OrderStatus.valueOf(resultSet.getString("status")));

        instantiateItems(order);
        return order;
    }

    private void instantiateItems(Order order) throws SQLException {
        String sql = "SELECT * FROM order_products WHERE order_id = ?";

        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStmt = connection.prepareStatement(sql);

        preparedStmt.setInt(1, order.getId());
        ResultSet resultSet = preparedStmt.executeQuery();

        ProductPersistenceService productService = PersistenceServiceFactory.createProductService();
        while (resultSet.next()) {
            Product product = productService.find(resultSet.getInt("product_id"));
            Double quantity = resultSet.getDouble("product_quantity");
            Boolean itemIncluded = resultSet.getBoolean("product_included");
            OrderItem item = new OrderItem(product, quantity);
            order.addItem(item, itemIncluded);
        }
    }


}
