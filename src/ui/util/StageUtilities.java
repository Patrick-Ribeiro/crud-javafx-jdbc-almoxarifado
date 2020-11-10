package ui.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StageUtilities {

    private static double xOffset = 0F;
    private static double yOffset = 0F;

    public static Stage currentStage(Event event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        return currentStage;
    }

    public static void makeStageDragable(Pane pane) {
        Stage stage = (Stage) pane.getScene().getWindow();

        pane.setOnMousePressed((event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        pane.setOnMouseDragged((event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            //stage.setOpacity(0.8F);
        });/*
        pane.setOnDragDone((event) -> {
            stage.setOpacity(1F);
        });
        pane.setOnMouseReleased((event) -> {
            stage.setOpacity(1F);
        });*/
    }
}
