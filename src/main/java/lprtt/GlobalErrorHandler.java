package lprtt;

import javafx.application.Platform;
import ui.MainController;

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
                    mensajeUsuario = "El nombre es demasiado largo (máx. 255 caracteres).";
                else if (sqlException.getMessage().contains("DESCRIPCION"))
                    mensajeUsuario = "La descripcion ingresada es demasiado larga";
                else
                    mensajeUsuario = "El texto ingresado es demasiado largo. ";
            else if (root instanceof SQLTimeoutException)
                mensajeUsuario = "La operación tardó demasiado. Intenta nuevamente.";
            else if (root instanceof SQLTransientConnectionException)
                mensajeUsuario = "No se pudo conectar a la base de datos.";
            else
                mensajeUsuario = "Ocurrió un error al acceder a la base de datos.";
        } else {
            mensajeUsuario = userMessage != null
                    ? userMessage
                    : "Ocurrió un error inesperado: " + getRootCause(throwable).getMessage();
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
