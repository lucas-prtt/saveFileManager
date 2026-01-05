package utils;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lprtt.ApplicationContext;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Optional;

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
        dialog.setTitle(I18nManager.get("Confirmar"));
        dialog.setHeaderText(I18nManager.get("EscribirParaConfirmar", texto));

        TextField textField = new TextField();
        textField.setPromptText("...");

        VBox box = new VBox(10);
        box.getChildren().add(textField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType aceptar = new ButtonType(I18nManager.get("Aceptar"), ButtonBar.ButtonData.OK_DONE);
        ButtonType rechazar = new ButtonType(I18nManager.get("Cancelar"), ButtonBar.ButtonData.CANCEL_CLOSE);
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

    public static Optional<String> inputText(String titulo, String mensaje, String defaultText) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(mensaje);

        TextField textField = new TextField();
        textField.setPromptText(defaultText);

        VBox box = new VBox(10);
        box.getChildren().add(textField);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType aceptar = new ButtonType(I18nManager.get("Aceptar"), ButtonBar.ButtonData.OK_DONE);
        ButtonType rechazar = new ButtonType(I18nManager.get("Cancelar"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(aceptar, rechazar);
        dialog.setResultConverter((b) -> {
            if(b.equals(aceptar)){
                return textField.getText();
            }
            else return null;
        });
        return dialog.showAndWait();
    }
    public static Optional<Path> inputDirectory(String titulo, String mensaje, Path defaultText) {
        Dialog<Path> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText(mensaje);

        TextField textField = new TextField();
        textField.setPromptText(defaultText.toString());

        Button btnElegirCarpeta = new Button("📁");
        btnElegirCarpeta.setTooltip(new Tooltip(I18nManager.get("ElegirCarpeta")));
        btnElegirCarpeta.setCursor(javafx.scene.Cursor.HAND);
        btnElegirCarpeta.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(I18nManager.get("SeleccionarCarpeta"));

            Window window = textField.getScene().getWindow();

            File selectedDir = chooser.showDialog(window);
            if (selectedDir != null) {
                textField.setText(selectedDir.getAbsolutePath());
            }
        });
        Button btnElegirArchivo = new Button("📄");
        btnElegirArchivo.setTooltip(new Tooltip(I18nManager.get("ElegirArchivo")));
        btnElegirArchivo.setCursor(javafx.scene.Cursor.HAND);
        btnElegirArchivo.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(I18nManager.get("SeleccionarArchivo"));

            Window window = textField.getScene().getWindow();

            File selectedDir = chooser.showOpenDialog(window);
            if (selectedDir != null) {
                textField.setText(selectedDir.getAbsolutePath());
            }
        });
        HBox textFieldBox = new HBox(textField, btnElegirArchivo, btnElegirCarpeta);




        VBox box = new VBox(10);
        box.getChildren().add(textFieldBox);
        box.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(box);

        ButtonType aceptar = new ButtonType(I18nManager.get("Aceptar"), ButtonBar.ButtonData.OK_DONE);
        ButtonType rechazar = new ButtonType(I18nManager.get("Cancelar"), ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(aceptar, rechazar);
        dialog.setResultConverter((b) -> {
            if(b.equals(aceptar)){
                try {
                    return Path.of(textField.getText());
                }catch (InvalidPathException e){
                    ApplicationContext.getMainController().showToast(I18nManager.get("DirectorioInvalido"));
                    return null;
                }
            }
            else return null;
        });
        return dialog.showAndWait();
    }
}
