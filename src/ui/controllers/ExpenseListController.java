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
import model.services.persistence.exceptions.DatabaseIntegrityException;
import ui.WindowLoader;
import ui.controllers.*;
import ui.listeners.DataChangeListener;
import ui.util.Alerts;
import ui.util.FXMLLocation;
import ui.util.StageUtilities;
import ui.util.TableViewUtilities;
import ui.util.controls.ButtonDelete;
import ui.util.controls.ButtonEdit;
import ui.util.controls.CheckBoxActive;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExpenseListController implements Initializable, DataChangeListener {

    ExpensePersistenceService persistenceService;

    @FXML
    private TableView<Expense> tableViewExpenses;
    private TableColumn<Expense, Integer> tableColumnDebit;
    private TableColumn<Expense, String> tableColumnDescription;
    private TableColumn<Expense, String> tableColumnType;
    private TableColumn<Expense, Expense> tableColumnEdit;
    private TableColumn<Expense, Expense> tableColumnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumns();
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
            Alerts.showAlert("Erro de conexão", "Não foi possível se conectar ao banco de dados",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initColumns() {
        tableColumnDebit = new TableColumn<>("Débito");
        tableColumnDescription = new TableColumn<>("Descrição");
        tableColumnType = new TableColumn<>("Tipo");
        tableColumnEdit = new TableColumn<>();
        tableColumnDelete = new TableColumn<>();

        // minimum value
        tableColumnEdit.setMinWidth(60);
        tableColumnDelete.setMinWidth(60);
        // maximum value
        tableColumnEdit.setMaxWidth(60);
        tableColumnDelete.setMaxWidth(60);
    }

    public void filterTable(List<Expense> expenseList) {
        tableColumnDebit.setCellValueFactory(new PropertyValueFactory("debit"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory("description"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory("type"));

        if (expenseList != null && expenseList.size() > 0) {
            List<TableColumn> columnList = new ArrayList<>();
            columnList.add(tableColumnDebit);
            columnList.add(tableColumnDescription);
            columnList.add(tableColumnType);
            columnList.add(tableColumnEdit);
            columnList.add(tableColumnDelete);
            TableViewUtilities.loadColumns(tableViewExpenses, columnList, expenseList);
        }
        initEditButtons();
        initDeleteButtons();
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
        try {
            Alerts.showConfirmation("Exclusão de despesa",
                    "Esta operação é irreverssível. Confirma?").ifPresent(type -> {
                if (type == ButtonType.OK) {
                    persistenceService.delete(expense.getDebit());
                    updateTable();
                }
            });
        } catch (DatabaseIntegrityException ex) {
            Alerts.showAlert("Erro ao excluir despesa",
                    "Não é possível excluir esta despesa, pois está associada à um grupo de produto",
                    "É necessário desassociar a despesa de qualquer grupo para que posso excluí-la", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
