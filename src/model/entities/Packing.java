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
}
