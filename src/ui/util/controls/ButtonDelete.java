package ui.util.controls;

import javafx.scene.control.Button;

public class ButtonDelete extends Button {

    public ButtonDelete() {
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        super.getStylesheets().add(style);
        super.getStyleClass().add("button-attention");

        setPrefSize(60, 20);
        super.setText("Excluir");
    }
}
