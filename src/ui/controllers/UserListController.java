package ui.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.jdbc.UserGroupPersistenceServiceJDBC;
import ui.WindowLoader;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserListController implements Initializable {

    UserPersistenceService userPersistenceService;

    @FXML
    Button buttonNew;
    @FXML
    Button buttonEdit;
    @FXML
    Button buttonDelete;
    @FXML
    Button buttonSearch;
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

    public void setUserPersistence(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    @FXML
    public void onTextFieldSearchKeyPressed(Event event) {
    }

    @FXML
    public void onButtonSearchAction(Event event) {
    }

    @FXML
    public void onButtonDeleteAction(Event event) {
        User user = tableViewUsers.getSelectionModel().getSelectedItem();
        if (user == null) {

        } else {
            userPersistenceService.delete(user.getCode());
            updateTable();
        }
    }

    @FXML
    public void onButtonEditAction(Event event) {
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
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de usuário");
        URL fxmlLocation = getClass().getResource("/ui/fxml/userFormDialog.fxml");
        WindowLoader.createEntityFormDialog(new User(), fxmlLocation, parentStage, dialogStage);
    }

    public void updateTable() {
        if (userPersistenceService == null) {
            throw new IllegalStateException("UserPersistenceService é nulo");
        }
        try {
            List<User> userList = userPersistenceService.findAll();
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
        } catch (DatabaseConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setHeaderText("Erro de conexão com o banco de daddos.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }


}
