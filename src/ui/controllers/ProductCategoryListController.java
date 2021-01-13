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
import ui.util.Alerts;
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
        WindowLoader.closePopup(StageUtilities.currentStage(event));
    }

    @FXML
    void onButtonDeleteAction(ActionEvent event) {
        ProductCategory category = listViewCategories.getSelectionModel().getSelectedItem();
        if (category != null)
            deleteCategory(category);
        else
            Alerts.showAlert("Erro ao excluir categoria",
                    "ategoria não selecionada", "É necessário selecionar uma categoria da lista", Alert.AlertType.ERROR);
    }

    @FXML
    void onButtonEditAction(ActionEvent event) {
        ProductCategory category = listViewCategories.getSelectionModel().getSelectedItem();
        if (category != null)
            editCategory(category);
        else
            Alerts.showAlert("Erro ao editar categoria",
                    "Categoria não selecionada", "É necessário selecionar uma categoria da lista", Alert.AlertType.ERROR);

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

    private void editCategory(ProductCategory category) {
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

    private void deleteCategory(ProductCategory category) {
        Alerts.showConfirmation("Exclusão de categoria",
                "Esta operação é irreverssível. Confirma?").ifPresent(type -> {
            if (type == ButtonType.OK) {
                try {
                    persistenceService.delete(category.getId());
                    updateList();
                } catch (DatabaseIntegrityException ex) {
                    Alerts.showAlert("Erro ao excluir categoria",
                            "Não é possível excluir esta categoria, pois está associada à um ou mais produtos",
                            "É necessário desassociar a categoria de qualquer produto para que possa excluí-la", Alert.AlertType.ERROR);
                }
            }
        });
    }
}