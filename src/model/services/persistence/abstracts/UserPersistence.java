package model.services.persistence.abstracts;

import model.entities.User;

import java.util.List;

public interface UserPersistence {

    public void insert(User user);

    public void delete(int id);

    public void update(User user);

    public List<User> findAll();

    public User find(int id);

}
