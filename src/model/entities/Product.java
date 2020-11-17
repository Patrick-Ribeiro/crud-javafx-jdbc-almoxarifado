package model.entities;

public class Product {

    private Integer internalCode;
    private String description;
    private String descriptionERP;
    private ProductCategory category;
    private ProductGroup group;
    private Packing packing;
    private Integer quantityPacking;
    private Buyer buyer;
    private Boolean active;

    public Product() {
    }

    public Product(Integer internalCode, String description, String descriptionERP, ProductCategory category,
                   ProductGroup group, Packing packing, Integer quantityPacking, Buyer buyer, Boolean active) {
        this.internalCode = internalCode;
        this.description = description;
        this.descriptionERP = descriptionERP;
        this.category = category;
        this.group = group;
        this.packing = packing;
        this.quantityPacking = quantityPacking;
        this.buyer = buyer;
        this.active = active;
    }

    public Integer getInternalCode() {
        return internalCode;
    }

    public void setInternalCode(Integer internalCode) {
        this.internalCode = internalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionERP() {
        return descriptionERP;
    }

    public void setDescriptionERP(String descriptionERP) {
        this.descriptionERP = descriptionERP;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public ProductGroup getGroup() {
        return group;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    public Packing getPacking() {
        return packing;
    }

    public void setPacking(Packing packing) {
        this.packing = packing;
    }

    public Integer getQuantityPacking() {
        return quantityPacking;
    }

    public void setQuantityPacking(Integer quantityPacking) {
        this.quantityPacking = quantityPacking;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Product{" +
                "description='" + description + '\'' +
                '}';
    }
}
