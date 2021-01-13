package ui.util;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class TableViewUtilities {

    public static <T> void loadColumns(TableView table, List<TableColumn> columns, List<T> items) {
        table.getColumns().clear();
        table.getColumns().setAll(columns);
        table.setItems(FXCollections.observableArrayList(items));
    }

}
