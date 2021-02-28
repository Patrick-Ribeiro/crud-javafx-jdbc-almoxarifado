package ui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.entities.Order;
import model.entities.User;
import model.entities.enums.OrderStatus;
import model.services.persistence.abstracts.OrderPersistenceService;
import model.services.persistence.exceptions.DatabaseConnectionException;
import ui.listeners.DataChangeListener;
import ui.util.Alerts;
import ui.util.TableViewUtilities;
import ui.util.controls.ButtonDelete;
import ui.util.controls.ButtonEdit;
import ui.util.controls.CheckBoxActive;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderListController implements Initializable, DataChangeListener {

    private OrderPersistenceService persistenceService;

    @FXML
    private Button buttonNew;
    @FXML
    private TextField textFieldSearch;
    @FXML
    private TableView<Order> tableViewOrders;

    TableColumn<Order, Integer> tableColumnId;
    TableColumn<Order, java.util.Date> tableColumnDate;
    TableColumn<Order, User> tableColumnRequester;
    TableColumn<Order, OrderStatus> tableColumnStatus;
    TableColumn<Order, Order> tableColumnEdit;
    TableColumn<Order, Order> tableColumnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumns();
    }

    public OrderPersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(OrderPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @FXML
    void onButtonNewAction(ActionEvent event) {

    }

    @FXML
    void onTextFieldSearchKeyPressed(KeyEvent event) {

    }

    private void initColumns() {
        tableColumnId = new TableColumn<>("Código");
        tableColumnDate = new TableColumn<>("Data");
        tableColumnRequester = new TableColumn<>("Requisitante");
        tableColumnStatus = new TableColumn<>("Status");
        tableColumnEdit = new TableColumn<>();
        tableColumnDelete = new TableColumn<>();

        // minimum value
        tableColumnEdit.setMinWidth(60);
        tableColumnDelete.setMinWidth(60);
        // maximum value
        tableColumnEdit.setMaxWidth(60);
        tableColumnDelete.setMaxWidth(60);
    }

    public void updateTable() {
        if (persistenceService == null) {
            throw new IllegalStateException("OrderPersistenceService é nulo");
        }
        try {
            List<Order> orderList = persistenceService.findAll();
            filterTable(orderList);
        } catch (DatabaseConnectionException ex) {
            Alerts.showAlert("Erro de conexão", "Não foi possível se conectar ao banco de dados",
                    ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public synchronized void filterTable(List<Order> orderList) {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnDate.setCellFactory(column -> {
            TableCell<Order, java.util.Date> cell = new TableCell<Order, java.util.Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                @Override
                protected void updateItem(java.util.Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty)
                        setText(null);
                    else
                        this.setText(format.format(item));
                }
            };
            return cell;
        });

        tableColumnRequester.setCellValueFactory(new PropertyValueFactory<>("requester"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnStatus.setCellFactory(column -> {
            TableCell<Order, OrderStatus> cell = new TableCell<Order, OrderStatus>() {
                @Override
                protected void updateItem(OrderStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty)
                        setText(null);
                    else
                        this.setText(item.toPortuguese());
                }
            };
            return cell;
        });

        if (orderList != null && orderList.size() > 0) {
            List<TableColumn> columnList = new ArrayList<>();
            columnList.add(tableColumnId);
            columnList.add(tableColumnDate);
            columnList.add(tableColumnRequester);
            columnList.add(tableColumnStatus);
            columnList.add(tableColumnEdit);
            columnList.add(tableColumnDelete);
            TableViewUtilities.loadColumns(tableViewOrders, columnList, orderList);
        }
        initEditButtons();
        initDeleteButtons();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Order, Order>() {
            private final Button button = new ButtonEdit();

            @Override
            protected void updateItem(Order item, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                //button.setOnAction(event -> createOrderForm(item));
            }
        });
    }

    private void initDeleteButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<Order, Order>() {
            private final Button button = new ButtonDelete();

            @Override
            protected void updateItem(Order item, boolean empty) {
                this.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                //button.setOnAction(event -> cancelOrder(item));
            }
        });
    }

    @Override
    public void onChangedData() {
        updateTable();
    }
}
