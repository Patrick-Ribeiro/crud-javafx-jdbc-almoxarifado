package ui.util.controls;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

public class ButtonEdit extends Button {

    public ButtonEdit() {
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        getStylesheets().add(style);
        getStyleClass().add("button-classic");

        setMinWidth(70);
        setPrefSize(60, 20);
        setTextAlignment(TextAlignment.CENTER);
        setText("Editar");
    }
}

