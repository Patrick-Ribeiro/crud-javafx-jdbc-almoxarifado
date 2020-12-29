package ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.entities.*;
import model.entities.enums.ExpenseType;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.ExpensePersistenceService;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
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

public class ExpenseFormDialogController implements Initializable, Notifier {

    private ExpensePersistenceService persistenceService;
    private Expense expense;
    private List<DataChangeListener> listeners = new ArrayList<>();

    @FXML
    private HBox hboxTitle;

    @FXML
    private Label labelErrorDebit;
    @FXML
    private Label labelErrorDescription;
    @FXML
    private Label labelErrorType;

    @FXML
    private JFXTextField textFieldDebit;
    @FXML
    private JFXTextField textFieldDescription;
    @FXML
    private JFXComboBox<ExpenseType> comboBoxType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNodes();
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
    void onHBoxTitleMouseMoved(MouseEvent event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    @FXML
    void onButtonConfirmAction(ActionEvent event) {
        Expense expense = getFormData();
        try {
            if (persistenceService.find(expense.getDebit()) == null)
                persistenceService.insert(expense);
            else
                persistenceService.update(expense);
            notifyListeners();
            WindowLoader.closePopup(StageUtilities.currentStage(event));
        } catch (PersistenceException ex) {
            Alerts.showAlert("Erro ao inserir", "Não foi possível inserir o produto",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onTextFieldUserCodeKeyPressed(KeyEvent event) {

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

    public void setEntity(Expense expense) {
        this.expense = expense;
    }

    public void setPersistenceService(ExpensePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    private void initNodes() {
        Constraints.setTextFieldInteger(textFieldDebit);
        Constraints.setTextFieldMaxLength(textFieldDescription, 200);
    }

    public void updateFormData() throws DatabaseConnectionException {
        if (expense == null) {
            throw new IllegalStateException("Despesa está nulo");
        }
        textFieldDebit.setText(String.valueOf(expense.getDebit()));
        textFieldDescription.setText(expense.getDescription());

        comboBoxType.setItems(FXCollections.observableArrayList(ExpenseType.values()));
        comboBoxType.getSelectionModel().select(expense.getType());

        if (expense.getDebit() != null) {
            textFieldDebit.setEditable(false);
            textFieldDebit.setDisable(false);
        }
    }

    public Expense getFormData() {
        if (expense.getDebit() == null) {
            expense = new Expense();
        }
        Integer debit = Util.tryParseToInt(textFieldDebit.getText());
        String description = textFieldDescription.getText();
        ExpenseType type = comboBoxType.getSelectionModel().getSelectedItem();

        expense.setDebit(debit);
        expense.setDescription(description);
        expense.setType(type);

        return expense;
    }

}
