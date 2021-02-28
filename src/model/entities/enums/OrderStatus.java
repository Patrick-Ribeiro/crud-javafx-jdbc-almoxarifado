package model.entities.enums;

public enum OrderStatus {
    PENDING("Pendente"),
    INCLUDED("Incluso"),
    COMPLETED("Completo"),
    CANCELED("Cancelado");

    private final String portuguese;

    OrderStatus(String portuguese) {
        this.portuguese = portuguese;
    }

    public String toPortuguese() {
        return portuguese;
    }
}
