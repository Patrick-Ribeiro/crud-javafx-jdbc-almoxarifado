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
import model.entities.Product;
import model.entities.ProductCategory;
import model.entities.ProductGroup;
import model.entities.User;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.ProductPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.exceptions.DatabaseIntegrityException;
import ui.WindowLoader;
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

public class ProductListController implements Initializable, DataChangeListener {

    ProductPersistenceService persistenceService;

    @FXML
    private TableView<Product> tableViewProducts;

    private TableColumn<Product, Integer> tableColumnInternalCode;
    private TableColumn<Product, String> tableColumnDescription;
    private TableColumn<Product, String> tableColumnDescriptionERP;
    private TableColumn<Product, ProductCategory> tableColumnCategory;
    private TableColumn<Product, Product> tableColumnActive;
    private TableColumn<Product, Product> tableColumnEdit;
    private TableColumn<Product, Product> tableColumnDelete;

    public void setPersistenceService(ProductPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    //<editor-fold desc="Initializers">
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumns();
    }

    private void initColumns() {
        tableColumnInternalCode = new TableColumn<>("Código");
        tableColumnDescription = new TableColumn<>("Descrição");
        tableColumnDescriptionERP = new TableColumn<>("Descrição ERP");
        tableColumnCategory = new TableColumn<>("Categoria");
        tableColumnActive = new TableColumn<>("Ativo");
        tableColumnEdit = new TableColumn<>();
        tableColumnDelete = new TableColumn<>();

        // minimum value
        tableColumnEdit.setMinWidth(60);
        tableColumnDelete.setMinWidth(60);
        tableColumnActive.setMinWidth(60);
        // maximum value
        tableColumnEdit.setMaxWidth(60);
        tableColumnDelete.setMaxWidth(60);
        tableColumnActive.setMaxWidth(60);
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Product, Product>() {
            private final Button button = new ButtonEdit();

            @Override
            protected void updateItem(Product product, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(product, empty);
                if (product == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createProductForm(product));
            }
        });
    }

    private void initActiveCheckBoxes() {
        tableColumnActive.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnActive.setCellFactory(param -> new TableCell<Product, Product>() {
            private final CheckBox checkBox = new CheckBoxActive();

            @Override
            protected void updateItem(Product product, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(product, empty);
                if (product == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(checkBox);
                checkBox.setSelected(product.isActive());
                checkBox.setOnAction(event -> {
                    product.setActive(checkBox.isSelected());
                    persistenceService.update(product);
                    updateTable();
                });
            }
        });
    }

    private void initDeleteButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<Product, Product>() {
            private final Button button = new ButtonDelete();

            @Override
            protected void updateItem(Product product, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(product, empty);
                if (product == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> deleteProduct(product));
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Events">
    @FXML
    void onButtonNewAction(ActionEvent event) {
        createProductForm(new Product());
    }

    @FXML
    void onButtonGroupsAction(ActionEvent event) {
        Stage currentStage = StageUtilities.currentStage(event);
        WindowLoader.createPopupScreen(FXMLLocation.PRODUCT_GROUP_LIST, currentStage,
                new Stage(), (ProductGroupListController controller) -> {
                    controller.setPersistenceService(PersistenceServiceFactory.createProductGroupService());
                    controller.updateTable();
                });
    }

    @FXML
    void onButtonCategoriesAction(ActionEvent event) {
        Stage currentStage = StageUtilities.currentStage(event);
        WindowLoader.createPopupScreen(FXMLLocation.PRODUCT_CATEGORY_LIST, currentStage,
                new Stage(), (ProductCategoryListController controller) -> {
                    controller.setPersistenceService(PersistenceServiceFactory.createProductCategoryService());
                    controller.updateList();
                });
    }

    @FXML
    void onButtonPackingsAction(ActionEvent event) {
        Stage currentStage = StageUtilities.currentStage(event);
        WindowLoader.createPopupScreen(FXMLLocation.PACKING_LIST, currentStage,
                new Stage(), (PackingListController controller) -> {
                    controller.setPersistenceService(PersistenceServiceFactory.createPackingService());
                    controller.updateList();
                });
    }

    @FXML
    void onTextFieldSearchKeyPressed(KeyEvent event) {

    }
    //</editor-fold>

    public void updateTable() {
        if (persistenceService == null) {
            throw new IllegalStateException("UserPersistenceService é nulo");
        }
        try {
            List<Product> productList = persistenceService.findAll();
            filterTable(productList);
        } catch (DatabaseConnectionException ex) {
            Alerts.showAlert("Erro de conexão", "Não foi possível se conectar ao banco de dados",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void filterTable(List<Product> productList) {
        tableColumnInternalCode.setCellValueFactory(new PropertyValueFactory("internalCode"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory("description"));
        tableColumnDescriptionERP.setCellValueFactory(new PropertyValueFactory("descriptionERP"));
        tableColumnCategory.setCellValueFactory(new PropertyValueFactory("category"));

        if (productList != null && productList.size() != 0) {
            List<TableColumn> columnList = new ArrayList<>();
            columnList.add(tableColumnInternalCode);
            columnList.add(tableColumnDescription);
            columnList.add(tableColumnDescriptionERP);
            columnList.add(tableColumnCategory);
            columnList.add(tableColumnActive);
            columnList.add(tableColumnEdit);
            columnList.add(tableColumnDelete);
            TableViewUtilities.loadColumns(tableViewProducts, columnList, productList);
        }
        initActiveCheckBoxes();
        initDeleteButtons();
        initEditButtons();
    }

    private void createProductForm(Product product) {
        Stage parentStage = (Stage) (tableViewProducts).getScene().getWindow();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de produto");

        WindowLoader.createPopupScreen(FXMLLocation.PRODUCT_FORM, parentStage,
                dialogStage, (ProductFormDialogController controller) -> {
                    controller.setEntity(product);
                    controller.setPersistenceService(PersistenceServiceFactory.createProductService());
                    controller.updateFormData();
                    controller.subscribeListener(this);
                });
    }

    private void deleteProduct(Product product) {
        Alerts.showConfirmation("Exclusão de produto",
                "Esta operação é irreverssível. Confirma?").ifPresent(type -> {
            if (type == ButtonType.OK) {
                try {
                    persistenceService.delete(product.getInternalCode());
                    updateTable();
                } catch (DatabaseIntegrityException ex) {
                    Alerts.showAlert("Erro ao excluir produto",
                            "Não é possível excluir este produto",
                            "", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
