package model.entities;

public class ProductCategory {

    private Integer id;
    private String description;

    public ProductCategory() {
    }

    public ProductCategory(Integer id, String description) {
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

    @Override
    public String toString() {
        return "ProductCategory{" +
                "description='" + description + '\'' +
                '}';
    }
}
