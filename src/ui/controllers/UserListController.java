package ui.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.User;
import ui.WindowLoader;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class UserListController implements Initializable {

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
    TableView<?> tableViewUsers;
    @FXML
    TableColumn<?, Integer> tableColumnUserCode;
    @FXML
    TableColumn<?, String> tableColumnUserName;
    @FXML
    TableColumn<?, String> tableColumnUserEmail;
    @FXML
    TableColumn<?, String> tableColumnUserTelephone;
    @FXML
    TableColumn<?, ?> tableColumnUserGroup;
    @FXML
    TableColumn<?, Boolean> tableColumnUserActive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        WindowLoader.createEntityFormDialog(new User(), fxmlLocation, parentStage, dialogStage);
    }

    public void updateTable() {
    }


}
