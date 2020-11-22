package ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.entities.ProductGroup;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.listeners.Notifier;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductGroupFormController implements Initializable, Notifier {

    private ProductGroup entity;
    private ProductGroupPersistenceService persistenceService;

    @FXML
    private JFXTextField textFieldDescription;
    @FXML
    private Label labelErrorDescription;
    @FXML
    private JFXComboBox<?> comboBoxExpense;
    @FXML
    private Label labelErrorExpense;
    @FXML
    private HBox hboxTitle;

    @FXML
    void onButtonCancelAction(ActionEvent event) {
        WindowLoader.closePopup(StageUtilities.currentStage(event));
    }

    @FXML
    void onButtonCloseAction(ActionEvent event) {
        WindowLoader.closePopup(StageUtilities.currentStage(event));
    }

    @FXML
    void onButtonConfirmAction(ActionEvent event) {

    }

    @FXML
    void onHBoxTitleMouseMoved(MouseEvent event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    public void setEntity(ProductGroup entity) {
        this.entity = entity;
    }

    public void setPersistenceService(ProductGroupPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void updateFormData() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void subscribeListener(DataChangeListener listener) {

    }

    @Override
    public void notifyListeners() {

    }
}
