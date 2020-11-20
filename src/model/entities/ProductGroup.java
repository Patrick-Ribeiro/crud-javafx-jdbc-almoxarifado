package model.entities;

public class ProductGroup {

    private Integer id;
    private String description;
    private Expense expense;

    public ProductGroup() {
    }

    public ProductGroup(Integer id, String description, Expense expense) {
        this.id = id;
        this.description = description;
        this.expense = expense;
    }

    public ProductGroup(Integer id, String description) {
        this.id = id;
        this.description = description;
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

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    @Override
    public String toString() {
        return description;
    }
}
