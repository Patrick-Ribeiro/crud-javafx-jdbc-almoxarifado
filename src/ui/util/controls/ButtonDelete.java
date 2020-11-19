package ui.util.controls;

import javafx.scene.control.Button;

public class ButtonDelete extends Button {

    public ButtonDelete() {
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        getStylesheets().add(style);
        getStyleClass().add("button-attention");

        super.setMinSize(70, 30);
        super.setMaxSize(75, 30);
        setText("Excluir");
    }
}
