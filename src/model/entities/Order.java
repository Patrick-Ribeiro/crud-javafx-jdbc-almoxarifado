package model.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private long id;
    private Date date;
    private User requester;
    private Map<OrderItem, Boolean> itemMap = new HashMap<>(); //Boolean represents the included or not included item
    private OrderStatus status = OrderStatus.PENDING;

    public void addItem(Product product, double quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("A quantidade do produto não pode ser menor ou igual a 0");
        itemMap.put(new OrderItem(product, quantity), Boolean.FALSE);
    }

    public void removeItem(Product product) {
        itemMap.remove(new OrderItem(product));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public Map<OrderItem, Boolean> getItemMap() {
        return itemMap;
    }

    public OrderStatus getStatus() {
        if (status == OrderStatus.PENDING) {
            for (int i = 0; i < itemMap.size(); i++) {
                boolean orderIncluded = true;
                if (itemMap.get(i) == Boolean.FALSE)
                    orderIncluded = false;
                if (orderIncluded) status = OrderStatus.INCLUDED;
            }
        }
        return status;
    }

}

enum OrderStatus {
    PENDING,
    INCLUDED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED;
}

class OrderItem {

    private Product product;
    private double quantity;

    public OrderItem() {
    }

    public OrderItem(Product product) {
        this.product = product;
    }

    public OrderItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem orderItem = (OrderItem) o;
        return product != null ? product.equals(orderItem.product) : orderItem.product == null;
    }

    @Override
    public int hashCode() {
        return product != null ? product.hashCode() : 0;
    }
}
