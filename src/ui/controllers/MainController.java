package ui.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.services.persistence.jdbc.PackingPersistenceServiceJBDC;
import model.services.persistence.jdbc.UserPersistenceServiceJDBC;
import ui.WindowLoader;
import ui.controllers.abstracts.AbstractMainController;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AbstractMainController {

    @FXML
    HBox hboxTitle;
    @FXML
    Button buttonClose;
    @FXML
    Button buttonMinimize;
    @FXML
    Button buttonMaximize;
    @FXML
    ScrollPane mainScrollPane;


    @Override
    public synchronized void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onButtonDashboardAction(Event event) {
        Stage currentStage = StageUtilities.currentStage(event);
        WindowLoader.createPopupScreen(getClass().getResource("/ui/fxml/PackingList.fxml"), currentStage,
                new Stage(), (PackingListController controller) -> {
                    controller.setPersistenceService(new PackingPersistenceServiceJBDC());
                    controller.updateList();
                });
    }

    @FXML
    public void onButtonOrdersAction(Event event) {

    }

    @FXML
    public void onButtonUsersAction(Event event) {
        loadScreen(getClass().getResource("/ui/fxml/userList.fxml"), (UserListController controller) -> {
            controller.setUserPersistence(new UserPersistenceServiceJDBC());
            controller.updateTable();
        });
    }

    @FXML
    public void onHBoxTitleMouseMoved(Event event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    @Override
    protected ScrollPane getMainScrollPane() {
        return mainScrollPane;
    }
}
