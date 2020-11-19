package ui.util.controls;

import javafx.scene.control.Button;

public class ButtonDelete extends Button {

    public ButtonDelete() {
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        getStylesheets().add(style);
        getStyleClass().add("button-attention");

        setMinWidth(70);
        setPrefSize(60, 20);
        setText("Excluir");
    }
}
