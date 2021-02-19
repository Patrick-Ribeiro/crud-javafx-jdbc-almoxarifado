package application;

import javafx.application.Application;
import javafx.stage.Stage;
import model.entities.User;
import ui.WindowLoader;

public class Program extends Application {

    private static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        new WindowLoader().start(primaryStage);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Program.currentUser = currentUser;
    }
}
