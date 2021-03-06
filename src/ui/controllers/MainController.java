package ui.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.jdbc.UserPersistenceServiceJDBC;
import ui.controllers.abstracts.AbstractMainController;
import ui.util.FXMLLocation;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController extends AbstractMainController {

    private MenuItem currentMenuItemOpen;

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

    }

    @FXML
    public void onButtonOrdersAction(ActionEvent event) {
        loadScreen(FXMLLocation.ORDER_LIST, (OrderListController controller) -> {
            controller.setPersistenceService(PersistenceServiceFactory.createOrderService());
            controller.updateTable();
        });
    }

    @FXML
    public void onButtonExpensesAction(ActionEvent event) {
        loadScreen(FXMLLocation.EXPENSE_LIST, (ExpenseListController controller) -> {
            controller.setPersistenceService(PersistenceServiceFactory.createExpenseService());
            controller.updateTable();
        });
        currentMenuItemOpen = MenuItem.EXPENSES;
    }

    @FXML
    void onButtonProductsAction(ActionEvent event) {
        loadScreen(FXMLLocation.PRODUCT_LIST, (ProductListController controller) -> {
            controller.setPersistenceService(PersistenceServiceFactory.createProductService());
            controller.updateTable();
        });
        updateCurrentMenuItemOpen(MenuItem.PRODUCTS);
        currentMenuItemOpen = MenuItem.PRODUCTS;
    }

    @FXML
    public void onButtonUsersAction(Event event) {
        loadScreen(FXMLLocation.USER_LIST, (UserListController controller) -> {
            controller.setPersistenceService(new UserPersistenceServiceJDBC());
            controller.updateTable();
        });
        currentMenuItemOpen = MenuItem.USERS;
    }

    @FXML
    public void onHBoxTitleMouseMoved(Event event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    @Override
    protected ScrollPane getMainScrollPane() {
        return mainScrollPane;
    }

    private void updateCurrentMenuItemOpen(MenuItem item) {
        currentMenuItemOpen = item;

        switch (currentMenuItemOpen) {
            case EXPENSES:
        }
    }
}

enum MenuItem {
    DASHBOARD,
    PRODUCTS,
    USERS,
    EXPENSES;
}
