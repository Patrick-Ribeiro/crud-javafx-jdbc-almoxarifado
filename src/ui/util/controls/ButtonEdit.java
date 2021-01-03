package ui.util.controls;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

public class ButtonEdit extends Button {

    public ButtonEdit() {
        String style = getClass().getResource("/ui/css/styles.css").toExternalForm();
        getStylesheets().add(style);
        getStyleClass().add("button-edit-and-delete");

        super.setMinSize(25, 25);
        super.setMaxSize(25, 25);

        Image image = new Image("/ui/media/icons/button-edit.png");
        setGraphic(new ImageView(image));
    }
}

