package ui;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.controllers.abstracts.AbstractMainController;
import util.Logs;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class WindowLoader extends Application {

    private static Scene mainScene;
    private static AbstractMainController mainController;

    @Override
    public void start(Stage primaryStage) {
        String locationMainFXMl = "/ui/fxml/main.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(locationMainFXMl));
            Parent root = loader.load();
            mainScene = new Scene(root, 1280, 720);
            mainController = loader.getController();

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("Almoxarifado");
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println("ERRO " + ex.getMessage()); //Implementar gerenciamento de logs
        }
    }

    public static synchronized <T> void createPopupScreen(URL fxmlLocation, Stage parentStage, Stage dialogStage, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Pane pane = loader.load();

            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            ColorAdjust colorAdjust = new ColorAdjust(0, 0.0, -0.2, 0);
            colorAdjust.setInput(new GaussianBlur());
            WindowLoader.getMainScene().getRoot().setEffect(colorAdjust);

            T controller = loader.getController();
            initializingAction.accept(controller);

            dialogStage.showAndWait();
        } catch (IOException ex) {
            System.out.println("ERRO " + ex.getMessage());
        }
    }

    public static void closePopupScreen(Stage stagePopup) {
        stagePopup.close();
        getMainScene().getRoot().setEffect(null);
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static AbstractMainController getMainController() {
        return mainController;
    }
}
