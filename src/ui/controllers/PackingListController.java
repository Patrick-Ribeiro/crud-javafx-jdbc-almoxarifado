package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;
import model.entities.Packing;
import model.entities.UserGroup;
import model.services.persistence.abstracts.PackingPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import ui.WindowLoader;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class PackingListController implements Initializable {

    private PackingPersistenceService persistenceService;

    @FXML
    private ListView<Packing> listViewPackings;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonEdit;

    @FXML
    private Button buttonNew;

    @FXML
    private HBox hboxTitle;

    @FXML
    private Button buttonClose;

    public void setPersistenceService(PackingPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onButtonCloseAction(ActionEvent event) {
        StageUtilities.currentStage(event).close();
        WindowLoader.getMainScene().getRoot().setEffect(null);
    }

    @FXML
    void onButtonDeleteAction(ActionEvent event) {

    }

    @FXML
    void onButtonEditAction(ActionEvent event) {

    }

    @FXML
    void onButtonNewAction(ActionEvent event) {
        TextInputDialog inputDialog = new TextInputDialog("Descrição");
        inputDialog.setHeaderText("Nova embalagem");
        inputDialog.initStyle(StageStyle.UNDECORATED);
        inputDialog.showAndWait();

        String description = inputDialog.getResult();
        if (description != null && description.length() > 0) {
            TextInputDialog inputDialog2 = new TextInputDialog("Abreviação");
            inputDialog2.initStyle(StageStyle.UNDECORATED);
            inputDialog2.showAndWait();
            String abbreviation = inputDialog2.getResult();

            if (abbreviation != null && abbreviation.length() > 0) {
                Packing packing = new Packing(description, abbreviation);
                persistenceService.insert(packing);
                updateList();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "A abreviação não pode ser nula", ButtonType.CLOSE);
                alert.setHeaderText("Abreviação inválida.");
                alert.initStyle(StageStyle.UNDECORATED);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A descrição não pode ser nula", ButtonType.CLOSE);
            alert.setHeaderText("Descrição inválida.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }

    @FXML
    void onHBoxTitleMouseMoved(MouseEvent event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    public void updateList() {
        if (persistenceService == null) {
            throw new IllegalStateException("PackingPersistenceService está nulo");
        }
        try {
            ObservableList<Packing> items = FXCollections.observableArrayList(persistenceService.findAll());
            listViewPackings.setItems(items);
            listViewPackings.refresh();
        } catch (DatabaseConnectionException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.setHeaderText("Erro de conexão com o banco de daddos.");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.showAndWait();
        }
    }
}
