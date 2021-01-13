package ui.util.controls;

import javafx.scene.control.CheckBox;

public class CheckBoxActive extends CheckBox {

    public CheckBoxActive() {
        super();
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        getStylesheets().add(style);
        getStyleClass().add("checkbox");
        super.setMinSize(25, 25);
        super.setMaxSize(25, 25);
    }
}
