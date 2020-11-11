package model.entities;

import model.entities.enums.TypeExpense;

public class Expense {

    private Integer id;
    private String description;
    private TypeExpense typeExpense;

    public Expense() {
    }

    public Expense(Integer id, String description, TypeExpense typeExpense) {
        this.id = id;
        this.description = description;
        this.typeExpense = typeExpense;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeExpense getTypeExpense() {
        return typeExpense;
    }

    public void setTypeExpense(TypeExpense typeExpense) {
        this.typeExpense = typeExpense;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "description='" + description + '\'' +
                ", typeExpense=" + typeExpense +
                '}';
    }
}
