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
import model.services.persistence.exceptions.PersistenceException;
import ui.WindowLoader;
import ui.util.Alerts;
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
        String description = showInputDescription();
        String abbreviation = null;
        if (description != null) {
            abbreviation = showInputAbbreviation();
            if (abbreviation != null) {
                insertPacking(new Packing(description, abbreviation));
            }
        }
    }

    private void insertPacking(Packing packing) {
        try {
            persistenceService.insert(packing);
            updateList();
        } catch (PersistenceException ex) {
            Alerts.showAlert("Erro de persistência", "Erro ao inserir embalagem",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public String showInputDescription() {
        TextInputDialog inputDialog = new TextInputDialog("Descrição");
        inputDialog.setHeaderText("Nova embalagem");
        inputDialog.initStyle(StageStyle.UNDECORATED);
        inputDialog.showAndWait();

        String description = inputDialog.getResult();
        if (!(description != null && description.length() > 0)) {
            Alerts.showAlert("Erro na descrição", "Descrição inválida",
                    "A descrição não pode ser nula", Alert.AlertType.ERROR);
            return null;
        }
        return description;
    }

    public String showInputAbbreviation() {
        TextInputDialog inputDialog2 = new TextInputDialog("Abreviação");
        inputDialog2.initStyle(StageStyle.UNDECORATED);
        inputDialog2.showAndWait();
        String abbreviation = inputDialog2.getResult();

        if (!(abbreviation.length() > 0 && abbreviation.length() <= 5)) {
            Alerts.showAlert("Nova embalagem", "Abreviação inválida",
                    "A abreviação deve conter de 1 à 5 caractéres", Alert.AlertType.ERROR);
            return null;
        }
        return abbreviation;
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
