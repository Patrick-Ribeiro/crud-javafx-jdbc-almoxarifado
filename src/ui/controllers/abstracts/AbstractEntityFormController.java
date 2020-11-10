package ui.controllers.abstracts;

import javafx.fxml.Initializable;

public abstract class AbstractEntityFormController<T> implements Initializable {

    private T entity;

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public abstract void updateFormData();

    public abstract T getFormData();
}
