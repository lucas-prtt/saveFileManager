package lprtt;

import javafx.application.Platform;
import ui.MainController;
import utils.I18nManager;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransientConnectionException;

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
        String mensajeUsuario;
        throwable.printStackTrace();
        Throwable root = getRootCause(throwable);
        if(root instanceof SQLException sqlException){
            if ("22001".equals(sqlException.getSQLState()))
                if (sqlException.getMessage().contains("NOMBRE"))
                    mensajeUsuario = I18nManager.get("NameTooLong");
                else if (sqlException.getMessage().contains("DESCRIPCION"))
                    mensajeUsuario = I18nManager.get("DescTooLong");
                else
                    mensajeUsuario = I18nManager.get("TextTooLong");
            else if (root instanceof SQLTimeoutException)
                mensajeUsuario = I18nManager.get("OperationTooLong");
            else if (root instanceof SQLTransientConnectionException)
                mensajeUsuario = I18nManager.get("NoDBConnection");
            else
                mensajeUsuario = I18nManager.get("DBUnknownError");
        } else {
            mensajeUsuario = userMessage != null
                    ? userMessage
                    : I18nManager.get("UnknownError", getRootCause(throwable).getMessage());
        }


        Platform.runLater(() -> {
            if (mainController != null) {
                mainController.showToast(mensajeUsuario);
            }
        });
    }
    private static Throwable getRootCause(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
        // Parece un trabalenguas esta funcion
    }
}
