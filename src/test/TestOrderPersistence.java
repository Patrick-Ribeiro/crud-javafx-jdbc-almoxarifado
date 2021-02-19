package test;

import model.entities.Order;
import model.entities.OrderItem;
import model.entities.Product;
import model.entities.User;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.OrderPersistenceService;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.jdbc.OrderPersistenceServiceJDBC;

public class TestOrderPersistence {

    public static void main(String[] args) {
        User user = PersistenceServiceFactory.createUserService().find(63);
        Order order = new Order();

        ProductPersistenceService productService = PersistenceServiceFactory.createProductService();
        Product product = productService.find(123456);
        Product product2 = productService.find(654321);

        order.addItem(product, 12);
        order.addItem(product2, 10);
        order.setRequester(user);

        OrderPersistenceService orderPersistenceService = new OrderPersistenceServiceJDBC();
        orderPersistenceService.insert(order);
    }
}
