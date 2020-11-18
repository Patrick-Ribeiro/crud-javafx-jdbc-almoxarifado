package ui.util.controls;

import javafx.scene.control.CheckBox;

public class CheckBoxActive extends CheckBox {

    public CheckBoxActive() {
        super();
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        this.getStylesheets().add(style);
        this.getStyleClass().add("checkbox");
        this.setPrefSize(20, 20);
    }
}
