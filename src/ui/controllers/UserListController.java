package ui.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.jdbc.UserGroupPersistenceServiceJDBC;
import model.services.persistence.jdbc.UserPersistenceServiceJDBC;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.util.Alerts;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserListController implements Initializable, DataChangeListener {

    UserPersistenceService persistenceService;

    @FXML
    Button buttonUserGroups;
    @FXML
    TextField textFieldSearch;
    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User, Integer> tableColumnUserCode;
    @FXML
    TableColumn<User, String> tableColumnUserName;
    @FXML
    TableColumn<User, String> tableColumnUserEmail;
    @FXML
    TableColumn<User, String> tableColumnUserTelephone;
    @FXML
    TableColumn<User, UserGroup> tableColumnUserGroup;
    @FXML
    TableColumn<User, Boolean> tableColumnUserActive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setPersistenceService(UserPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @FXML
    public void onTextFieldSearchKeyPressed(KeyEvent event) {
        String filterSearch = textFieldSearch.getText();
        if (filterSearch.length() <= 1 || filterSearch == null || filterSearch.equals("")) {
            filterTable(persistenceService.findAll());
        } else {
            filterTable(persistenceService.find(filterSearch));
        }
    }

    @FXML
    public void onButtonSearchAction(Event event) {
        String filterSearch = textFieldSearch.getText();
        filterTable(persistenceService.find(filterSearch));
    }

    @FXML
    public void onButtonDeleteAction(Event event) {
        User user = tableViewUsers.getSelectionModel().getSelectedItem();
        if (user == null) {

        } else {
            Alerts.showConfirmation("Exclusão de usuário", "Esta operação é irreverssível. Confirma?").ifPresent(type -> {
                if (type == ButtonType.YES) {
                    persistenceService.delete(user.getCode());
                    updateTable();
                }
            });
        }
    }

    @FXML
    public void onButtonEditAction(Event event) {
        Stage parentStage = StageUtilities.currentStage(event);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de usuário");
        URL fxmlLocation = getClass().getResource("/ui/fxml/userFormDialog.fxml");
        User user = tableViewUsers.getSelectionModel().getSelectedItem();

        WindowLoader.createPopupScreen(fxmlLocation, parentStage,
                dialogStage, (UserFormDialogController controller) -> {
                    controller.setUser(user);
                    controller.setUserPersistenceService(new UserPersistenceServiceJDBC());
                    controller.updateFormData();
                    controller.subscribeListener(this);
                });
    }

    @FXML
    public void onButtonUserGroupsAction(ActionEvent event) {
        URL fxmlLocation = getClass().getResource("/ui/fxml/userGroupList.fxml");
        Stage currentStage = StageUtilities.currentStage(event);

        WindowLoader.createPopupScreen(fxmlLocation, currentStage,
                new Stage(), (UserGroupListController controller) -> {
                    controller.setUserGroupPersistence(new UserGroupPersistenceServiceJDBC());
                    controller.updateList();
                });
    }

    @FXML
    public void onButtonNewAction(Event event) {
        Stage parentStage = StageUtilities.currentStage(event);
        URL fxmlLocation = getClass().getResource("/ui/fxml/userFormDialog.fxml");

        WindowLoader.createPopupScreen(fxmlLocation, parentStage,
                new Stage(), (UserFormDialogController controller) -> {
                    controller.setUser(new User());
                    controller.setUserPersistenceService(new UserPersistenceServiceJDBC());
                    controller.updateFormData();
                    controller.subscribeListener(this);
                });
    }

    public void updateTable() {
        if (persistenceService == null) {
            throw new IllegalStateException("UserPersistenceService é nulo");
        }
        try {
            List<User> userList = persistenceService.findAll();
            filterTable(userList);
        } catch (DatabaseConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setHeaderText("Erro de conexão com o banco de daddos.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }

    public void filterTable(List<User> userList) {
        tableViewUsers.getItems().clear();
        tableColumnUserCode.setCellValueFactory(new PropertyValueFactory("code"));
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumnUserEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tableColumnUserTelephone.setCellValueFactory(new PropertyValueFactory("telephone"));
        tableColumnUserGroup.setCellValueFactory(new PropertyValueFactory("group"));
        tableColumnUserActive.setCellValueFactory(new PropertyValueFactory("active"));
        if (userList.size() != 0) {
            tableViewUsers.setItems(FXCollections.observableArrayList(userList));
            tableViewUsers.getSelectionModel().select(0);
        }
    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
