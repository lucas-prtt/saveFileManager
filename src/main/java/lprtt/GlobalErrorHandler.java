package lprtt;

import javafx.application.Platform;
import ui.MainController;

public class GlobalErrorHandler {

    private static MainController mainController;

    private GlobalErrorHandler() {}


    public static void init(MainController controller) {
        mainController = controller;

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            handle(throwable);
        });

        System.setProperty(
                "javafx.exceptionHandler",
                GlobalErrorHandler.class.getName()
        );
    }

    public static void handle(Throwable throwable) {
        handle("Error: " + throwable.getMessage(), throwable);
    }

    public static void handle(String userMessage, Throwable throwable) {
        throwable.printStackTrace();

        Platform.runLater(() -> {
            if (mainController != null) {
                mainController.showToast(throwable.getMessage());
            }
        });
    }
}
