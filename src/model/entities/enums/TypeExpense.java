package model.entities.enums;

public enum TypeExpense {
    SELLING("S"),
    CONSUMPTION("C");

    private final String abreviation;

    TypeExpense(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getAbreviation() {
        return abreviation;
    }

}
