package model.entities;

import model.entities.enums.MovementType;

import java.time.LocalDate;

public class Movement {

    private LocalDate date;
    private Product product;
    private MovementType type;
    private Double quantity;
    private Double previousInventory;
    private Double finalInventory;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPreviousInventory() {
        return previousInventory;
    }

    public void setPreviousInventory(Double previousInventory) {
        this.previousInventory = previousInventory;
    }

    public Double getFinalInventory() {
        return finalInventory;
    }

    public void setFinalInventory(Double finalInventory) {
        this.finalInventory = finalInventory;
    }
}
