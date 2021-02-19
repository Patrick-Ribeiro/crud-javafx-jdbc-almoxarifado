package model.entities;

import model.entities.enums.OrderStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

