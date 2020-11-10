package ui.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.User;
import ui.Loader;
import ui.controllers.abstracts.AbstractEntityFormController;
import ui.util.StageUtilities;

import java.io.IOException;
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
        createFormDialog(new User(), "/ui/fxml/userFormDialog.fxml", parentStage);
    }

    public void updateTable() {
    }

    public void createFormDialog(User entity, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            AbstractEntityFormController<User> controller = loader.getController();
            controller.setEntity(entity);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dados do usuário");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Loader.getMainScene().getRoot().setEffect(new GaussianBlur());
            dialogStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace(); //implementar gerenciamento de logs
        }
    }


}
