package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Loader extends Application {

    private static Scene mainScene;

    @Override
    public void start(Stage primaryStage) {
        String locationMainFXMl = "/ui/fxml/main.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(locationMainFXMl));
            Parent root = loader.load();
            mainScene = new Scene(root, 1280, 720);

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("Almoxarifado");
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println("ERRO " + ex.getMessage()); //Implementar gerenciamento de logs
        } catch (IllegalStateException ex) {
            System.out.println("ERRO no arquivo FXML " + locationMainFXMl); //Implementar gerenciamento de logs
        }
    }

    public static Scene getMainScene() {
        return mainScene;
    }
}
