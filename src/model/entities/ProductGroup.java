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
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductGroup that = (ProductGroup) o;

        if (!id.equals(that.id)) return false;
        return expense != null ? expense.equals(that.expense) : that.expense == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (expense != null ? expense.hashCode() : 0);
        return result;
    }
}
