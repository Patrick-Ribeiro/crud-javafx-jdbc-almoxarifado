package model.entities.enums;

public enum TypeExpense {
    SELLING("S", "Venda"),
    CONSUMPTION("C", "Consumo");

    private final String abreviation;
    private final String portuguese;

    TypeExpense(String abreviation, String portuguese) {
        this.abreviation = abreviation;
        this.portuguese = portuguese;
    }

    public String getAbreviation() {
        return abreviation;
    }

    public String toPortuguese() {
        return portuguese;
    }
}
