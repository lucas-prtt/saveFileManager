package utils;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Dialogs {
    public static boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(mensaje);

        return alert.showAndWait()
                .filter(r -> r == ButtonType.OK)
                .isPresent();
    }
    private static boolean confirmarEscribirTexto(String texto) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Confirmar");
        dialog.setHeaderText("Escriba \"" + texto + "\" para confirmar");

        TextField textField = new TextField();
        textField.setPromptText("...");

        VBox box = new VBox(10);
        box.getChildren().add(textField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType aceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType rechazar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(aceptar, rechazar);
        dialog.getDialogPane().lookupButton(aceptar).addEventFilter(ActionEvent.ACTION, e -> {
            if(!texto.equals(textField.getText())) {
                e.consume();
            }
        });
        dialog.setResultConverter((b) -> b == aceptar); // Solo llega aca si el texto es correcto
        return dialog.showAndWait().orElse(false);
    }
    public static boolean confirmarDoble(String mensaje, String texto){
        return confirmar(mensaje) && confirmarEscribirTexto(texto);
    }

    public static Optional<String> inputText(String titulo, String mensaje) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(mensaje);

        TextField textField = new TextField();
        textField.setPromptText("...");

        VBox box = new VBox(10);
        box.getChildren().add(textField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType aceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType rechazar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(aceptar, rechazar);
        dialog.setResultConverter((b) -> {
            if(b.equals(aceptar)){
                return textField.getText();
            }
            else return null;
        });
        return dialog.showAndWait();
    }
}
