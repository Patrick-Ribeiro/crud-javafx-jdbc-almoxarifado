package ui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Expense;
import model.entities.Product;
import model.entities.ProductCategory;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.ExpensePersistenceService;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import ui.WindowLoader;
import ui.controllers.*;
import ui.listeners.DataChangeListener;
import ui.util.Alerts;
import ui.util.FXMLLocation;
import ui.util.StageUtilities;
import ui.util.controls.ButtonDelete;
import ui.util.controls.ButtonEdit;
import ui.util.controls.CheckBoxActive;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ExpenseListController implements Initializable, DataChangeListener {

    ExpensePersistenceService persistenceService;

    @FXML
    private TableView<Expense> tableViewExpenses;
    @FXML
    private TableColumn<Expense, Integer> tableColumnDebit;
    @FXML
    private TableColumn<Expense, String> tableColumnDescription;
    @FXML
    private TableColumn<Expense, String> tableColumnType;
    @FXML
    TableColumn<Expense, Expense> tableColumnEdit;
    @FXML
    TableColumn<Expense, Expense> tableColumnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onButtonNewAction(ActionEvent event) {
        createExpenseForm(new Expense());
    }

    @FXML
    void onTextFieldSearchKeyPressed(KeyEvent event) {

    }

    public void setPersistenceService(ExpensePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void updateTable() {
        if (persistenceService == null) {
            throw new IllegalStateException("PersistenceService é nulo");
        }
        try {
            List<Expense> expenseList = persistenceService.findAll();
            filterTable(expenseList);
        } catch (DatabaseConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setHeaderText("Erro de conexão com o banco de daddos.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }

    public void filterTable(List<Expense> expenseList) {
        tableViewExpenses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewExpenses.getItems().clear();
        tableColumnDebit.setCellValueFactory(new PropertyValueFactory("debit"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory("description"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory("type"));

        initDeleteButtons();
        initEditButtons();

        if (expenseList != null && expenseList.size() != 0) {
            tableViewExpenses.setItems(FXCollections.observableArrayList(expenseList));
            tableViewExpenses.getSelectionModel().select(0);
        }
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Expense, Expense>() {
            private final Button button = new ButtonEdit();

            @Override
            protected void updateItem(Expense item, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createExpenseForm(item));
            }
        });
    }

    private void initDeleteButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<Expense, Expense>() {
            private final Button button = new ButtonDelete();

            @Override
            protected void updateItem(Expense item, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> deleteExpense(item));
            }
        });
    }

    private void createExpenseForm(Expense expense) {
        Stage parentStage = (Stage) (tableViewExpenses).getScene().getWindow();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de despesa");

        WindowLoader.createPopupScreen(FXMLLocation.EXPENSE_FORM, parentStage,
                dialogStage, (ExpenseFormDialogController controller) -> {
                    controller.setEntity(expense);
                    controller.setPersistenceService(PersistenceServiceFactory.createExpenseService());
                    controller.updateFormData();
                    controller.subscribeListener(this);
                });
    }

    private void deleteExpense(Expense expense) {
        Alerts.showConfirmation("Exclusão de despesa",
                "Esta operação é irreverssível. Confirma?").ifPresent(type -> {
            if (type == ButtonType.OK) {
                persistenceService.delete(expense.getDebit());
                updateTable();
            }
        });
    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
