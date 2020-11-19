package ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import ui.WindowLoader;

import java.util.Optional;

public class Alerts {

    public static void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setResizable(true);

        setEffects(alert);
        alert.show();
    }

    public static Optional<ButtonType> showConfirmation(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content);
        alert.setTitle("Confirmação");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);

        setEffects(alert);
        return alert.showAndWait();
    }

    private static void setEffects(Alert alert) {
        alert.setOnShown(event -> {
            ColorAdjust colorAdjust = new ColorAdjust(0, 0.0, -0.2, 0);
            WindowLoader.getMainScene().getRoot().setEffect(colorAdjust);
        });
        alert.setOnCloseRequest(event -> {
            WindowLoader.getMainScene().getRoot().setEffect(null);
        });
    }
}
