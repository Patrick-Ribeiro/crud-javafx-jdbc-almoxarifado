package ui.util.controls;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

public class ButtonEdit extends Button {

    public ButtonEdit() {
        super();
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        this.getStylesheets().add(style);
        this.getStyleClass().add("button-classic");

        setPrefSize(60, 20);
        this.setTextAlignment(TextAlignment.CENTER);
        this.setText("Editar");
    }
}

