package ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.entities.*;
import model.services.persistence.PersistenceServiceFactory;
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

public class ProductFormDialogController implements Initializable, Notifier {

    private ProductPersistenceService persistenceService;
    private Product product;
    private List<DataChangeListener> listeners = new ArrayList<>();

    @FXML
    private HBox hboxTitle;

    @FXML
    private Label labelErrorCode;
    @FXML
    private Label labelErrorDescriptionErp;
    @FXML
    private Label labelErrorDescription;
    @FXML
    private Label labelErrorUserQuantityPacking;
    @FXML
    private Label labelErrorCategory;
    @FXML
    private Label labelErrorGroup;
    @FXML
    private Label labelErrorBuyer;
    @FXML
    private Label labelErrorPacking;

    @FXML
    private JFXTextField textFieldInternalCode;
    @FXML
    private JFXTextField textFieldDescriptionERP;
    @FXML
    private JFXTextField textFieldDescription;
    @FXML
    private JFXComboBox<ProductGroup> comboBoxGroup;
    @FXML
    private JFXComboBox<ProductCategory> comboBoxCategory;
    @FXML
    private JFXComboBox<User> comboBoxBuyer;
    @FXML
    private JFXComboBox<Packing> comboBoxPacking;
    @FXML
    private JFXTextField textFieldQuantityPacking;

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
        Product product = getFormData();
        try {
            if(product.getInternalCode() == null)
                persistenceService.insert(product);
            else
                persistenceService.update(product);
            WindowLoader.closePopup(StageUtilities.currentStage(event));
            notifyListeners();
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

    public void setEntity(Product product) {
        this.product = product;
    }

    public void setPersistenceService(ProductPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    private void initNodes() {
        Constraints.setTextFieldInteger(textFieldInternalCode);
        Constraints.setTextFieldInteger(textFieldQuantityPacking);
        Constraints.setTextFieldMaxLength(textFieldDescription, 45);
        Constraints.setTextFieldMaxLength(textFieldDescriptionERP, 45);
    }

    public void updateFormData() throws DatabaseConnectionException {
        if (product == null) {
            throw new IllegalStateException("Produto está nulo");
        }
        textFieldInternalCode.setText("" + product.getInternalCode());
        textFieldDescription.setText(product.getDescription());
        textFieldDescriptionERP.setText(product.getDescriptionERP());
        textFieldQuantityPacking.setText("" + product.getQuantityPacking());

        comboBoxGroup.setItems(FXCollections.observableArrayList(PersistenceServiceFactory.createProductGroupService().findAll()));
        comboBoxCategory.setItems(FXCollections.observableArrayList(PersistenceServiceFactory.createProductCategoryService().findAll()));
        UserGroup groupBuyer = PersistenceServiceFactory.createUserGroupService().find(2);
        comboBoxBuyer.setItems(FXCollections.observableArrayList(PersistenceServiceFactory.createUserService().find(groupBuyer)));
        comboBoxPacking.setItems(FXCollections.observableArrayList(PersistenceServiceFactory.createPackingService().findAll()));

        comboBoxGroup.getSelectionModel().select(product.getGroup());
        comboBoxCategory.getSelectionModel().select(product.getCategory());
        comboBoxPacking.getSelectionModel().select(product.getPacking());
        comboBoxBuyer.getSelectionModel().select(product.getBuyer());

        if (product.getInternalCode() != null) {
            textFieldInternalCode.setEditable(false);
            textFieldInternalCode.setDisable(false);
        }
    }

    public Product getFormData() {
        if (product.getInternalCode() == null) {
            product = new Product();
            product.setActive(false);
        }

        Integer internalCode = Util.tryParseToInt(textFieldInternalCode.getText());
        String description = textFieldDescription.getText();
        String descriptionERP = textFieldDescriptionERP.getText();
        ProductGroup group = comboBoxGroup.getSelectionModel().getSelectedItem();
        ProductCategory category = comboBoxCategory.getSelectionModel().getSelectedItem();
        Packing packing = comboBoxPacking.getSelectionModel().getSelectedItem();
        Integer quantityPacking = Util.tryParseToInt(textFieldQuantityPacking.getText());
        User buyer = comboBoxBuyer.getSelectionModel().getSelectedItem();

        product.setInternalCode(internalCode);
        product.setDescription(description);
        product.setDescriptionERP(descriptionERP);
        product.setGroup(group);
        product.setCategory(category);
        product.setPacking(packing);
        product.setQuantityPacking(quantityPacking);
        product.setBuyer(buyer);

        return product;
    }

}
