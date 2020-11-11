package ui.controllers;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.abstracts.UserPersistence;
import ui.WindowLoader;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserListController implements Initializable {

    UserPersistence userPersistence;

    @FXML
    Button buttonNew;
    @FXML
    Button buttonEdit;
    @FXML
    Button buttonDelete;
    @FXML
    Button buttonSearch;
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

    public void setUserPersistence(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @FXML
    public void onTextFieldSearchKeyPressed(Event event) {
    }

    @FXML
    public void onButtonSearchAction(Event event) {
    }

    @FXML
    public void onButtonDeleteAction(Event event) {
    }

    @FXML
    public void onButtonEditAction(Event event) {
    }

    @FXML
    public void onButtonNewAction(Event event) {
        Stage parentStage = StageUtilities.currentStage(event);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de usuário");
        URL fxmlLocation = getClass().getResource("/ui/fxml/userFormDialog.fxml");

        User user = new User(1234, "User test", new UserGroup(), true);
        user.setTelephone("(62)9.9999-9999");
        user.setEmail("user@user.com");
        WindowLoader.createEntityFormDialog(user, fxmlLocation, parentStage, dialogStage);
    }

    public void updateTable() {
        if (userPersistence == null) {
            throw new IllegalStateException("UserPersistence é nulo");
        }
        List<User> userList = userPersistence.findAll();
        tableColumnUserCode.setCellValueFactory(new PropertyValueFactory("code"));
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory("name"));
        tableColumnUserEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tableColumnUserTelephone.setCellValueFactory(new PropertyValueFactory("telephone"));
        tableColumnUserGroup.setCellValueFactory(new PropertyValueFactory("group"));
        tableColumnUserActive.setCellValueFactory(new PropertyValueFactory("active"));
        tableViewUsers.setItems(FXCollections.observableArrayList(userList));
        tableViewUsers.getSelectionModel().select(0);
    }


}
