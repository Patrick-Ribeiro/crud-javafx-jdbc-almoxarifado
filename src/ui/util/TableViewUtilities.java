package ui.util;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.entities.User;
import ui.util.controls.ButtonEdit;


import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

public class TableViewUtilities {

    public static <T> void loadColumns(TableView table, List<TableColumn> columns, List<T> items) {
        table.getColumns().clear();
        table.getColumns().setAll(columns);
        table.setItems(FXCollections.observableArrayList(items));
    }

}
