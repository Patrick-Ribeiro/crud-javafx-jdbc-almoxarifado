package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Packing;
import model.entities.ProductCategory;
import model.entities.UserGroup;
import model.services.persistence.abstracts.ProductCategoryPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.exceptions.DatabaseIntegrityException;
import ui.WindowLoader;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.Locale;
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
        WindowLoader.closePopup(StageUtilities.currentStage(event));
    }

    @FXML
    void onButtonDeleteAction(ActionEvent event) {
        ProductCategory category = listViewCategories.getSelectionModel().getSelectedItem();
        if (category == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "É necessário selecionar uma categoria da lista", ButtonType.CLOSE);
            alert.setHeaderText("Categoria não selecionada");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir o grupo de usuários "
                    + category.getDescription() + "?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Exclusão de categoria");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.YES) {
                    try {
                        persistenceService.delete(category.getId());
                        updateList();
                    } catch (DatabaseIntegrityException ex) {
                        Alert alertError = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
                        alertError.setHeaderText("Não é possível excluir esta categoria, pois está associada à um ou mais produtos.");
                        alertError.initStyle(StageStyle.UNDECORATED);
                        alertError.show();
                    }
                }
            });
        }
    }

    @FXML
    void onButtonEditAction(ActionEvent event) {
        ProductCategory category = listViewCategories.getSelectionModel().getSelectedItem();
        if (category == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "É necessário selecionar uma categoria da lista", ButtonType.CLOSE);
            alert.setHeaderText("Categoria não selecionada");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.show();
        } else {
            TextInputDialog inputDialog = new TextInputDialog(category.getDescription());
            inputDialog.setHeaderText("Editar categoria");
            inputDialog.initStyle(StageStyle.UNDECORATED);
            inputDialog.showAndWait();

            String newDescription = inputDialog.getResult();
            if (newDescription != null) {
                category.setDescription(newDescription);
                persistenceService.update(category);
                updateList();
            }
        }
    }

    @FXML
    void onButtonNewAction(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog("Descrição");
        inputDialog.setHeaderText("Nova Categoria");
        inputDialog.initStyle(StageStyle.UNDECORATED);
        inputDialog.showAndWait();

        String description = inputDialog.getResult();
        if (description != null) {
            ProductCategory category = new ProductCategory(description);
            persistenceService.insert(category);
            updateList();
        }
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