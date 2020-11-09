package ui.controllers.abstracts;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import ui.util.StageUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public abstract class AbstractMainController implements Initializable {

    protected boolean maximized = false;

    @FXML
    public void onButtonCloseAction(Event event) {
        StageUtilities.currentStage(event).close();
    }

    @FXML
    public void onButtonMinimizeAction(Event event) {
        StageUtilities.currentStage(event).setIconified(true);
    }

    @FXML
    public void onButtonMaximizeAction(Event event) {
        maximized = !maximized;
        StageUtilities.currentStage(event).setMaximized(maximized);
    }

    protected abstract ScrollPane getMainScrollPane();

    public synchronized <T> void loadScreen(URL location, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(location);
            VBox updatedVBox = loader.load();

            VBox oldVBox = (VBox) getMainScrollPane().getContent();
            oldVBox.getChildren().clear();
            oldVBox.getChildren().addAll(updatedVBox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);
        } catch (IOException ex) {
            System.out.println("ERRO " + ex.getMessage());
        } catch (IllegalStateException ex) {
            System.out.println("ERRO no arquivo FXML " + location.toString());
        }
    }
}

