package ui.util.controls;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonDelete extends Button {

    public ButtonDelete() {
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        getStylesheets().add(style);
        getStyleClass().add("button-edit-and-delete");

        super.setMinSize(25, 25);
        super.setMaxSize(25, 25);

        Image image = new Image("/ui/media/icons/button-delete.png");
        setGraphic(new ImageView(image));
    }
}
