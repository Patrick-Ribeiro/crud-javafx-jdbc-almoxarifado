package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Packing;
import model.entities.ProductCategory;
import model.services.persistence.abstracts.ProductCategoryPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import ui.WindowLoader;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductCategoryListController implements Initializable {

    private ProductCategoryPersistenceService persistenceService;

    @FXML
    private ListView<ProductCategory> listViewCategories;

    @FXML
    private HBox hboxTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setPersistenceService(ProductCategoryPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @FXML
    void onButtonCloseAction(ActionEvent event) {
        Stage currentStage = StageUtilities.currentStage(event);
        WindowLoader.closePopupScreen(currentStage);
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

    public void updateList() {
        if (persistenceService == null) {
            throw new IllegalStateException("ProductCategoryPersistenceService está nulo");
        }
        try {
            ObservableList<ProductCategory> items = FXCollections.observableArrayList(persistenceService.findAll());
            listViewCategories.setItems(items);
            listViewCategories.refresh();
        } catch (DatabaseConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setHeaderText("Erro de conexão com o banco de daddos.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }
}