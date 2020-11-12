package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserGroupPersistenceService;
import ui.util.StageUtilities;

public class UserGroupListController {

    UserGroupPersistenceService userGroupPersistenceService;

    @FXML
    private ListView<UserGroup> listViewUserGroups;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonNew;

    @FXML
    private HBox hboxTitle;

    @FXML
    private Button buttonClose;

    public void setUserGroupPersistence(UserGroupPersistenceService userGroupPersistenceService) {
        this.userGroupPersistenceService = userGroupPersistenceService;
    }

    @FXML
    public void onButtonCloseAction(ActionEvent event) {
        StageUtilities.currentStage(event).close();
    }

    @FXML
    public void onButtonDeleteAction(ActionEvent event) {
    }

    @FXML
    public void onButtonEditAction(ActionEvent event) {
        UserGroup userGroup = listViewUserGroups.getSelectionModel().getSelectedItem();
        if (userGroup == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "É necessário selecionar um grupo da lista", ButtonType.CLOSE);
            alert.setHeaderText("Grupo não selecionado");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.show();
        } else {
            TextInputDialog inputDialog = new TextInputDialog(userGroup.getDescription());
            inputDialog.setHeaderText("Editar grupo");
            inputDialog.initStyle(StageStyle.UNDECORATED);
            inputDialog.showAndWait();

            String newDescription = inputDialog.getResult();
            if (newDescription != null) {
                userGroup.setDescription(newDescription);
                userGroupPersistenceService.update(userGroup);
                updateList();
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
            userGroupPersistenceService.insert(userGroup);
            updateList();
        }
    }

    @FXML
    public void onHBoxTitleMouseMoved(Event event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    public void updateList() {
        if (userGroupPersistenceService == null) {
            throw new IllegalStateException("UserGroupPersistenceService está nulo");
        }
        ObservableList<UserGroup> items = FXCollections.observableArrayList(userGroupPersistenceService.findAll());
        listViewUserGroups.setItems(items);
        listViewUserGroups.refresh();
    }

}
