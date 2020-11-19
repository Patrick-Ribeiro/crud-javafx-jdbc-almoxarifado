package ui.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.UserPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.jdbc.UserGroupPersistenceServiceJDBC;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.util.Constraints;
import ui.util.StageUtilities;
import ui.util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserFormDialogController implements Initializable {

    private UserPersistenceService persistenceService;
    private User user;

    @FXML
    HBox hboxTitle;
    @FXML
    private JFXTextField textFieldUserCode;
    @FXML
    private Label labelErrorUserCode;
    @FXML
    private JFXTextField textFieldName;
    @FXML
    private JFXTextField textFieldEmail;
    @FXML
    private JFXTextField textFieldTelephone;
    @FXML
    private Label labelErrorName;
    @FXML
    private JFXComboBox<UserGroup> comboBoxGroup;
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

    private List<DataChangeListener> listeners = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
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
    public void onHBoxTitleMouseMoved(Event event) {
        StageUtilities.makeStageDragable(hboxTitle);
    }

    @FXML
    void onTextFieldUserCodeKeyPressed(Event event) {
        if (!textFieldUserCode.isEditable())
            labelErrorUserCode.setText("Não é possível editar o código");
    }

    @FXML
    synchronized void onButtonConfirmAction(ActionEvent event) {
        if (persistenceService == null)
            throw new IllegalStateException("UserPersistenceService está nulo");

        User userFromForm = getFormData();
        if (persistenceService.find(userFromForm.getCode()) == null)
            persistenceService.insert(userFromForm);
        else
            persistenceService.update(userFromForm);

        WindowLoader.closePopup(StageUtilities.currentStage(event));
        notifyChanges();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(textFieldUserCode);
        Constraints.setTextFieldMaxLength(textFieldUserCode, 4);
        Constraints.setTextFieldMaxLength(textFieldName, 35);
        Constraints.setTextFieldMaxLength(textFieldTelephone, 14);
    }

    public void setUserPersistenceService(UserPersistenceService userPersistenceService) {
        persistenceService = userPersistenceService;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void subscribeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    private void notifyChanges() {
        for (DataChangeListener listener : listeners) {
            listener.onChangedData();
        }
    }

    public void updateFormData() throws DatabaseConnectionException {
        if (user == null) {
            throw new IllegalStateException("Usuário está nulo");
        }
        textFieldUserCode.setText(String.valueOf(user.getCode()));
        textFieldName.setText(user.getName());
        textFieldEmail.setText(user.getEmail());
        textFieldTelephone.setText(user.getTelephone());
        comboBoxGroup.setItems(FXCollections.observableArrayList(PersistenceServiceFactory.createUserGroupService().findAll()));
        comboBoxGroup.getSelectionModel().select(user.getGroup());
        checkBoxActive.setSelected(user.isActive());

        if (user.getCode() != null) {
            textFieldUserCode.setEditable(false);
            textFieldUserCode.setDisable(false);
        }
    }

    public User getFormData() {
        Integer code = Util.tryParseToInt(textFieldUserCode.getText());
        String name = textFieldName.getText();
        String email = textFieldEmail.getText();
        String telephone = textFieldTelephone.getText();
        UserGroup userGroup = comboBoxGroup.getSelectionModel().getSelectedItem();
        Boolean active = checkBoxActive.isSelected();

        User user = new User(code, name, userGroup, active);
        user.setEmail(email);
        user.setTelephone(telephone);
        return user;
    }

}
