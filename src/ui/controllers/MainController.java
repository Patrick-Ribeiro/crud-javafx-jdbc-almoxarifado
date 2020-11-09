package ui.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import ui.controllers.abstracts.AbstractMainController;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AbstractMainController {

    @FXML
    Button buttonClose;
    @FXML
    Button buttonMinimize;
    @FXML
    Button buttonMaximize;
    @FXML
    ScrollPane mainScrollPane;
    @FXML
    Button buttonDashboard;
    @FXML
    Button buttonOrders;
    @FXML
    Button buttonUsers;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onButtonDashboardAction(Event event) {

    }

    @FXML
    public void onButtonOrdersAction(Event event) {

    }

    @FXML
    public void onButtonUsersAction(Event event) {

    }

    @Override
    protected ScrollPane getMainScrollPane() {
        return mainScrollPane;
    }
}
