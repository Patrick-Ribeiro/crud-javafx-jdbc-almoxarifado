package ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import model.entities.User;
import ui.Loader;
import ui.controllers.abstracts.AbstractEntityFormController;
import ui.util.StageUtilities;

import java.net.URL;
import java.util.ResourceBundle;

public class UserFormDialogController extends AbstractEntityFormController<User> {

    @FXML
    private JFXTextField textFieldUserCode;

    @FXML
    private Label labelErrorUserCode;

    @FXML
    private JFXTextField textFieldName;

    @FXML
    private JFXTextField textFieldEmail;

    @FXML
    private Label labelErrorName;

    @FXML
    private JFXComboBox<?> comboBoxGroup;

    @FXML
    private Label labelErrorUserGroup;

    @FXML
    private CheckBox checkBoxActive;

    @FXML
    private Label labelErrorTelephone;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonConfirm;

    @FXML
    private Button buttonClose;

    @FXML
    void onButtonCancelAction(ActionEvent event) {
        StageUtilities.currentStage(event).close();
        Loader.getMainScene().getRoot().setEffect(null);
    }

    @FXML
    void onButtonConfirmAction(ActionEvent event) {

    }

    @FXML
    void onButtonCloseAction(ActionEvent event) {
        StageUtilities.currentStage(event).close();
        Loader.getMainScene().getRoot().setEffect(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void updateFormData() {

    }

    @Override
    public User getFormData() {
        return null;
    }
}