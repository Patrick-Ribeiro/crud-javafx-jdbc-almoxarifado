package model.services.persistence.abstracts;

import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.exceptions.PersistenceException;

import java.util.List;

public interface UserGroupPersistenceService {

    /**
     * Inclui um novo grupo de usuário na base de dados.
     *
     * @param userGroup objeto contendo as informações do grupo a ser incluído.
     */
    public void insert(UserGroup userGroup);

    public void delete(int id);

    public void update(UserGroup userGroup);

    public List<UserGroup> findAll();

    public UserGroup find(int id);
}
