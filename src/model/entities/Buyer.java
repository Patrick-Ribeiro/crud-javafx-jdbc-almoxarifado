package model.entities;

public class Buyer extends User {

    public Buyer() {
    }

    public Buyer(Integer code, String name, UserGroup group, boolean active) {
        super(code, name, group, active);
    }
}
