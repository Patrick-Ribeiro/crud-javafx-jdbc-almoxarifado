package model.entities;

import model.entities.enums.ExpenseType;

import java.util.Objects;

public class Expense {

    private Integer debit;
    private String description;
    private ExpenseType type;

    public Expense() {
    }

    public Expense(Integer debit, String description, ExpenseType type) {
        this.debit = debit;
        this.description = description;
        this.type = type;
    }

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return debit.equals(expense.debit) &&
                Objects.equals(description, expense.description) &&
                type == expense.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(debit, description, type);
    }

    @Override
    public String toString() {
        return type.toPortuguese() + " - " + description;
    }
}