package ui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.User;
import model.entities.UserGroup;
import model.services.persistence.PersistenceServiceFactory;
import model.services.persistence.abstracts.UserPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import model.services.persistence.jdbc.UserGroupPersistenceServiceJDBC;
import ui.WindowLoader;
import ui.listeners.DataChangeListener;
import ui.util.Alerts;
import ui.util.FXMLLocation;
import ui.util.TableViewUtilities;
import ui.util.controls.ButtonDelete;
import ui.util.controls.ButtonEdit;
import ui.util.StageUtilities;
import ui.util.controls.CheckBoxActive;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class UserListController implements Initializable, DataChangeListener {

    UserPersistenceService persistenceService;

    @FXML
    Button buttonUserGroups;
    @FXML
    TextField textFieldSearch;
    @FXML
    TableView<User> tableViewUsers;

    TableColumn<User, Integer> tableColumnUserCode;
    TableColumn<User, String> tableColumnUserName;
    TableColumn<User, String> tableColumnUserEmail;
    TableColumn<User, String> tableColumnUserTelephone;
    TableColumn<User, UserGroup> tableColumnUserGroup;
    TableColumn<User, User> tableColumnUserActive;
    TableColumn<User, User> tableColumnEdit;
    TableColumn<User, User> tableColumnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumns();
    }

    public void setPersistenceService(UserPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @FXML
    public void onTextFieldSearchKeyPressed(KeyEvent event) {
        String filterSearch = textFieldSearch.getText();
        if (filterSearch.length() <= 1) {
            filterTable(persistenceService.findAll());
        } else {
            filterTable(persistenceService.find(filterSearch));
        }
    }

    @FXML
    public void onButtonSearchAction(Event event) {
        String filterSearch = textFieldSearch.getText();
        filterTable(persistenceService.find(filterSearch));
    }

    @FXML
    public void onButtonUserGroupsAction(ActionEvent event) {
        Stage currentStage = StageUtilities.currentStage(event);

        WindowLoader.createPopupScreen(FXMLLocation.USER_GROUP_LIST, currentStage,
                new Stage(), (UserGroupListController controller) -> {
                    controller.setUserGroupPersistence(new UserGroupPersistenceServiceJDBC());
                    controller.updateList();
                });
    }

    @FXML
    public void onButtonNewAction(Event event) {
        createUserForm(new User());
    }

    public void updateTable() {
        if (persistenceService == null) {
            throw new IllegalStateException("UserPersistenceService é nulo");
        }
        try {
            List<User> userList = persistenceService.findAll();
            filterTable(userList);
        } catch (DatabaseConnectionException ex) {
            Alerts.showAlert("Erro de conexão", "Não foi possível se conectar ao banco de dados",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initColumns() {
        tableColumnUserCode = new TableColumn<>("Código ERP");
        tableColumnUserName = new TableColumn<>("Nome");
        tableColumnUserEmail = new TableColumn<>("Email");
        tableColumnUserTelephone = new TableColumn<>("Telefone");
        tableColumnUserGroup = new TableColumn<>("Grupo");
        tableColumnUserActive = new TableColumn<>("Ativo");
        tableColumnEdit = new TableColumn<>();
        tableColumnDelete = new TableColumn<>();

        // minimum value
        tableColumnEdit.setMinWidth(60);
        tableColumnDelete.setMinWidth(60);
        tableColumnUserActive.setMinWidth(60);
        // maximum value
        tableColumnEdit.setMaxWidth(60);
        tableColumnDelete.setMaxWidth(60);
        tableColumnUserActive.setMaxWidth(60);
    }

    public synchronized void filterTable(List<User> userList) {
        tableColumnUserCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnUserTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        tableColumnUserGroup.setCellValueFactory(new PropertyValueFactory<>("group"));

        if (userList != null && userList.size() > 0) {
            List<TableColumn> columnList = new ArrayList<>();
            columnList.add(tableColumnUserCode);
            columnList.add(tableColumnUserName);
            columnList.add(tableColumnUserEmail);
            columnList.add(tableColumnUserTelephone);
            columnList.add(tableColumnUserGroup);
            columnList.add(tableColumnUserActive);
            columnList.add(tableColumnEdit);
            columnList.add(tableColumnDelete);
            TableViewUtilities.loadColumns(tableViewUsers, columnList, userList);
        }
        initEditButtons();
        initDeleteButtons();
        initActiveCheckBoxes();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<User, User>() {
            private final Button button = new ButtonEdit();

            @Override
            protected void updateItem(User user, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createUserForm(user));
            }
        });
    }

    private void initActiveCheckBoxes() {
        tableColumnUserActive.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnUserActive.setCellFactory(param -> new TableCell<User, User>() {
            private final CheckBox checkBox = new CheckBoxActive();

            @Override
            protected void updateItem(User user, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(checkBox);
                checkBox.setSelected(user.isActive());
                checkBox.setOnAction(event -> {
                    user.setActive(checkBox.isSelected());
                    persistenceService.update(user);
                    updateTable();
                });
            }
        });
    }

    private void initDeleteButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<User, User>() {
            private final Button button = new ButtonDelete();

            @Override
            protected void updateItem(User user, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> deleteUser(user));
            }
        });
    }

    private void createUserForm(User user) {
        Stage parentStage = (Stage) (tableViewUsers).getScene().getWindow();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Formulário de usuário");

        WindowLoader.createPopupScreen(FXMLLocation.USER_FORM_DIALOG, parentStage,
                dialogStage, (UserFormDialogController controller) -> {
                    controller.setUser(user);
                    controller.setUserPersistenceService(PersistenceServiceFactory.createUserService());
                    controller.updateFormData();
                    controller.subscribeListener(this);
                });
    }

    private void deleteUser(User user) {
        Alerts.showConfirmation("Exclusão de usuário",
                "Esta operação é irreverssível. Confirma?").ifPresent(type -> {
            if (type == ButtonType.OK) {
                persistenceService.delete(user.getCode());
                updateTable();
            }
        });
    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
