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
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.util.FXMLLocation;
import ui.util.StageUtilities;
import ui.util.controls.ButtonDelete;
import ui.util.controls.ButtonEdit;
import ui.util.controls.CheckBoxActive;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductListController implements Initializable, DataChangeListener {

    ProductPersistenceService persistenceService;

    @FXML
    private TableView<Product> tableViewProducts;
    @FXML
    private TableColumn<Product, Integer> tableColumnInternalCode;
    @FXML
    private TableColumn<Product, String> tableColumnDescription;
    @FXML
    private TableColumn<Product, String> tableColumnDescriptionERP;
    @FXML
    private TableColumn<Product, ProductCategory> tableColumnCategory;
    @FXML
    TableColumn<Product, Product> tableColumnActive;
    @FXML
    TableColumn<Product, Product> tableColumnEdit;
    @FXML
    TableColumn<Product, Product> tableColumnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onButtonDeleteAction(ActionEvent event) {

    }

    @FXML
    void onButtonEditAction(ActionEvent event) {

    }

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

    public void setPersistenceService(ProductPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void updateTable() {
        if (persistenceService == null) {
            throw new IllegalStateException("UserPersistenceService é nulo");
        }
        try {
            List<Product> productList = persistenceService.findAll();
            filterTable(productList);
        } catch (DatabaseConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setHeaderText("Erro de conexão com o banco de daddos.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }

    public void filterTable(List<Product> productList) {
        tableViewProducts.getItems().clear();
        tableColumnInternalCode.setCellValueFactory(new PropertyValueFactory("internalCode"));
        tableColumnDescription.setCellValueFactory(new PropertyValueFactory("description"));
        tableColumnDescriptionERP.setCellValueFactory(new PropertyValueFactory("descriptionERP"));
        tableColumnCategory.setCellValueFactory(new PropertyValueFactory("category"));

        initActiveCheckBoxes();
        initDeleteButtons();
        initEditButtons();

        if (productList != null && productList.size() != 0) {
            tableViewProducts.setItems(FXCollections.observableArrayList(productList));
            tableViewProducts.getSelectionModel().select(0);
        }
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

    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
