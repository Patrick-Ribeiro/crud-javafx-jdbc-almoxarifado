package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserGroupPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.exceptions.DatabaseIntegrityException;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.listeners.Notifier;
import ui.util.Alerts;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserGroupListController implements Initializable, Notifier {

    private UserGroupPersistenceService persistenceService;
    private List<DataChangeListener> listeners = new ArrayList<>();

    @FXML
    private ListView<UserGroup> listViewUserGroups;
    @FXML
    private HBox hboxTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUserGroupPersistence(UserGroupPersistenceService userGroupPersistenceService) {
        this.persistenceService = userGroupPersistenceService;
    }

    @FXML
    public void onButtonCloseAction(ActionEvent event) {
        WindowLoader.closePopup(StageUtilities.currentStage(event));
    }

    @FXML
    public void onButtonDeleteAction(ActionEvent event) {
        UserGroup userGroup = listViewUserGroups.getSelectionModel().getSelectedItem();
        if (userGroup == null) {
            Alerts.showAlert("Erro", "Grupo não selecionado", " necessário selecionar um grupo da lista", Alert.AlertType.ERROR);
        } else {
            Alerts.showConfirmation("Exclusão de grupo", "Deseja excluir o grupo de usuários?").ifPresent(type -> {
                if (type == ButtonType.OK) {
                    deleteUserGroup(userGroup);
                }
            });
        }
    }

    @FXML
    public void onButtonEditAction(ActionEvent event) {
        UserGroup userGroup = listViewUserGroups.getSelectionModel().getSelectedItem();
        if (userGroup == null) {
            Alerts.showAlert("Erro", "Grupo não selecionado", " necessário selecionar um grupo da lista", Alert.AlertType.ERROR);
        } else {
            TextInputDialog inputDialog = new TextInputDialog(userGroup.getDescription());
            inputDialog.setHeaderText("Editar grupo");
            inputDialog.initStyle(StageStyle.UNDECORATED);
            inputDialog.showAndWait();

            String newDescription = inputDialog.getResult();
            if (newDescription != null) {
                userGroup.setDescription(newDescription);
                updateUserGroup(userGroup);
            }
        }
    }

    @FXML
    public void onButtonNewAction(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog("Descrição");
        inputDialog.setHeaderText("Novo grupo");
        inputDialog.initStyle(StageStyle.UNDECORATED);
        inputDialog.showAndWait();

        String description = inputDialog.getResult();
        if (description != null) {
            UserGroup userGroup = new UserGroup(description);
            persistenceService.insert(userGroup);
            updateList();
        }
    }

    @FXML
    public void onHBoxTitleMouseMoved(Event event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    public void updateList() {
        if (persistenceService == null) {
            throw new IllegalStateException("UserGroupPersistenceService está nulo");
        }
        try {
            ObservableList<UserGroup> items = FXCollections.observableArrayList(persistenceService.findAll());
            listViewUserGroups.setItems(items);
            listViewUserGroups.refresh();

            if (items.size() > 0) {
                listViewUserGroups.getSelectionModel().select(0);
            }
        } catch (DatabaseConnectionException ex) {
            Alerts.showAlert("Erro", "Erro de conexão com o banco de dados", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteUserGroup(UserGroup userGroup) {
        try {
            persistenceService.delete(userGroup.getId());
            updateList();
        } catch (DatabaseIntegrityException ex) {
            Alerts.showAlert("Erro de integridade", "Não é possível excluir este grupo",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateUserGroup(UserGroup userGroup) {
        try {
            persistenceService.update(userGroup);
            updateList();
        } catch (DatabaseConnectionException ex) {
            Alerts.showAlert("Erro de integridade", "Não é possível excluir este grupo, pois está associado à um ou mais usuários.",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void subscribeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyListeners() {
        for (DataChangeListener listener : listeners) {
            listener.onChangedData();
        }
    }
}
