package model.services.persistence.abstracts;

import model.entities.User;
import model.services.persistence.exceptions.PersistenceException;

import java.util.List;

public interface UserPersistence {

    /**
     * Inclui um novo usuário na base de dados.
     *
     * @param user objeto do tipo User contendo as informações do novo
     *             usuário
     * @throws PersistenceException caso um usuário com o mesmo código já exista na
     *                              base de dados.
     */
    public void insert(User user) throws PersistenceException;

    public void delete(int id);

    public void update(User user);

    public List<User> findAll();

    public User find(int id);

}
