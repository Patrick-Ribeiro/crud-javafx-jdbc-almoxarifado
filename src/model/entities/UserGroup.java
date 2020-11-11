package model.entities;

public class UserGroup {

    private Integer id;
    private String description;

    public UserGroup() {
    }

    public UserGroup(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public UserGroup(String description) {
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
        return description;
    }
}
