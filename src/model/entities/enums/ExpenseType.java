package model.entities.enums;

public enum ExpenseType {
    SELLING("S", "Venda"),
    CONSUMPTION("C", "Consumo");

    private final String abreviation;
    private final String portuguese;

    ExpenseType(String abreviation, String portuguese) {
        this.abreviation = abreviation;
        this.portuguese = portuguese;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public String toPortuguese() {
        return portuguese;
    }

    @Override
    public String toString() {
        return portuguese;
    }
}
