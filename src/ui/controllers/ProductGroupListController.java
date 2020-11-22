package ui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.entities.Expense;
import model.entities.ProductGroup;
import model.entities.User;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.ProductGroupPersistenceService;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.util.FXMLLocation;
import ui.util.StageUtilities;
import ui.util.controls.ButtonDelete;
import ui.util.controls.ButtonEdit;
import ui.util.controls.CheckBoxActive;

public class ProductGroupListController implements DataChangeListener {

    private ProductGroupPersistenceService persistenceService;

    @FXML
    private HBox hboxTitle;
    @FXML
    private TableView<ProductGroup> tableViewGroups;
    @FXML
    private TableColumn<ProductGroup, Integer> tableColumnId;
    @FXML
    private TableColumn<ProductGroup, String> tableColumnDescription;
    @FXML
    private TableColumn<ProductGroup, Expense> tableColumDebitExpense;
    @FXML
    private TableColumn<ProductGroup, Expense> tableColumnDescriptionExpense;
    @FXML
    TableColumn<ProductGroup, ProductGroup> tableColumnEdit;
    @FXML
    TableColumn<ProductGroup, ProductGroup> tableColumnDelete;


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
        createProductGroupForm(new ProductGroup());
    }

    @FXML
    void onHBoxTitleMouseMoved(MouseEvent event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    public void updateTable() {
        tableColumDebitExpense.setCellValueFactory(new PropertyValueFactory<>("expense"));
        tableColumDebitExpense.setCellFactory(param -> new TableCell<ProductGroup, Expense>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText("" + item.getId());
                } else {
                    setText(null);
                }
            }
        });
        tableColumnDescriptionExpense.setCellValueFactory(new PropertyValueFactory<>("expense"));
        tableColumnDescriptionExpense.setCellFactory(param -> new TableCell<ProductGroup, Expense>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(item.getDescription());
                } else {
                    setText(null);
                }
            }
        });
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableViewGroups.setItems(FXCollections.observableArrayList(persistenceService.findAll()));
        tableViewGroups.getSelectionModel().select(0);

        initDeleteButtons();
        initEditButtons();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<ProductGroup, ProductGroup>() {
            private final Button button = new ButtonEdit();

            @Override
            protected void updateItem(ProductGroup item, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createProductGroupForm(item));
            }
        });
    }

    private void initDeleteButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<ProductGroup, ProductGroup>() {
            private final Button button = new ButtonDelete();

            @Override
            protected void updateItem(ProductGroup item, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> deteleProductGroup(item));
            }
        });
    }

    private void createProductGroupForm(ProductGroup group) {
        Stage parentStage = (Stage) (tableViewGroups).getScene().getWindow();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de usuário");

        WindowLoader.createPopupScreen(FXMLLocation.PRODUCT_GROUP_FORM, parentStage,
                dialogStage, (ProductGroupFormController controller) -> {
                    controller.setEntity(group);
                    controller.setPersistenceService(PersistenceServiceFactory.createProductGroupService());
                    controller.updateFormData();
                    controller.subscribeListener(this);
                });
    }

    private void deteleProductGroup(ProductGroup group) {

    }

    @Override
    public void onChangedData() {

    }
}
