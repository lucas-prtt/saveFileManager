package lprtt;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.MainController;

public class Main {
    public static void main(String[] args) {
        ApplicationContext.init();
        JavaFxApp.launch(JavaFxApp.class, args);
    }

    public static class JavaFxApp extends Application {

        @Override
        public void start(Stage stage) {
            MainController controller = ApplicationContext.getMainController();
            controller.setStage(stage);
        }

        @Override
        public void stop() {
        }
    }
}