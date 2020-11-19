package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.ProductCategory;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import ui.WindowLoader;
import ui.util.StageUtilities;

public class ProductGroupListController {

    private ProductGroupPersistenceService persistenceService;

    @FXML
    private HBox hboxTitle;

    public void setPersistenceService(ProductGroupPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @FXML
    void onButtonExpenseAction(ActionEvent event) {

    }

    @FXML
    void onButtonCloseAction(ActionEvent event) {
        WindowLoader.closePopup(StageUtilities.currentStage(event));
    }

    @FXML
    void onButtonDeleteAction(ActionEvent event) {

    }

    @FXML
    void onButtonEditAction(ActionEvent event) {

    }

    @FXML
    void onButtonNewAction(ActionEvent event) {

    }

    @FXML
    void onHBoxTitleMouseMoved(MouseEvent event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    public void updateTable() {

    }

}
