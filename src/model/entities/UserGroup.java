package model.entities;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserGroup userGroup = (UserGroup) o;

        return id.equals(userGroup.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
