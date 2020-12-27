package ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.entities.Expense;
import model.entities.ProductGroup;
import model.entities.User;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
import model.services.persistence.exceptions.PersistenceException;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.listeners.Notifier;
import ui.util.Alerts;
import ui.util.Constraints;
import ui.util.StageUtilities;
import ui.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductGroupFormController implements Initializable, Notifier {

    private ProductGroup entity;
    private ProductGroupPersistenceService persistenceService;
    private List<DataChangeListener> listeners = new ArrayList<>();

    @FXML
    private JFXTextField textFieldId;
    @FXML
    private JFXTextField textFieldDescription;
    @FXML
    private Label labelErrorDescription;
    @FXML
    private JFXComboBox<Expense> comboBoxExpense;
    @FXML
    private Label labelErrorExpense;
    @FXML
    private HBox hboxTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    @FXML
    void onHBoxTitleMouseMoved(MouseEvent event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

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
        if (persistenceService == null)
            throw new IllegalStateException("ProductGroupPersistenceService está nulo");

        ProductGroup groupForm = getFormData();
        try {
            if (persistenceService.find(groupForm.getId()) == null)
                persistenceService.insert(groupForm);
            else
                persistenceService.update(groupForm);

            WindowLoader.closePopup(StageUtilities.currentStage(event));
            notifyListeners();
        } catch (PersistenceException ex) {
            Alerts.showAlert("Erro de persistência", "Erro ao persistir o grupo",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(textFieldId);
        Constraints.setTextFieldMaxLength(textFieldId, 5);
        Constraints.setTextFieldMaxLength(textFieldDescription, 45);
    }

    public void setEntity(ProductGroup entity) {
        this.entity = entity;
    }

    public void setPersistenceService(ProductGroupPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("O grupo de produto está nulo");
        }
        textFieldId.setText("" + entity.getId());
        textFieldDescription.setText(entity.getDescription());
        comboBoxExpense.setItems(FXCollections.observableArrayList(PersistenceServiceFactory.createExpenseService().findAll()));
        comboBoxExpense.getSelectionModel().select(entity.getExpense());
    }


    public ProductGroup getFormData() {
        if (entity == null) {
            entity = new ProductGroup();
        }
        entity.setId(Util.tryParseToInt(textFieldId.getText()));
        entity.setDescription(textFieldDescription.getText());
        entity.setExpense(comboBoxExpense.getSelectionModel().getSelectedItem());
        return entity;
    }

    @Override
    public void subscribeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyListeners() {
        for (DataChangeListener listener : listeners) {
            listener.onChangedData();
        }
    }
}
