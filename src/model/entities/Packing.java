package model.entities;

public class Packing {

    private Integer id;
    private String description;
    private String abbreviation;

    public Packing(String description, String abbreviation) {
        this.description = description;
        this.abbreviation = abbreviation;
    }

    public Packing(Integer id, String description, String abbreviation) {
        this.id = id;
        this.description = description;
        this.abbreviation = abbreviation;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String toString() {
        return abbreviation + " - " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Packing packing = (Packing) o;

        if (!id.equals(packing.id)) return false;
        if (description != null ? !description.equals(packing.description) : packing.description != null) return false;
        return abbreviation != null ? abbreviation.equals(packing.abbreviation) : packing.abbreviation == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        return result;
    }
}
