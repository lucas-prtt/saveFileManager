package ui.tabWrapper;

import servicios.DirectorySecurity;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import ui.MainController;
import utils.Buttons;
import utils.I18nManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public class TabSettings extends TabWrapper{

    private DirectorySecurity directorySecurity;
    @Override
    public void init(MainController mainController) {
        super.init(mainController);
        this.directorySecurity = controller.getDirectorySecurity();
    }

    @Override
    public VBox getContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(10));
        VBox card = new VBox(20);
        card.getStyleClass().add("inicio-card");
        Label labelOpciones = new Label();
        labelOpciones.setText("Configuración: Que directorios puede modificar el programa?");
        labelOpciones.getStyleClass().add("inicio-title");

        card.getChildren().addAll(labelOpciones, whiteList(), blackList(), botonOverride(), botonDeleteFiles(), botonMaximoTamañoArchivo());
        content.getChildren().add(card);
        return content;
    }

    private VBox whiteList(){

        Label labelOpciones = new Label();
        labelOpciones.setText(I18nManager.get("Whitelist"));

        VBox content = new VBox(3);
        ListView<Path> listPaths = new ListView<>();
        listPaths.getItems().addAll(directorySecurity.getWhitelist());

        listPaths.setCellFactory(lv -> new ListCell<Path>() {

            private final Label lblPath = new Label();
            private final Button btnEliminar = new Button("✖");
            private final Region spacer = new Region();
            private final HBox content = new HBox(10);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);

                btnEliminar.getStyleClass().addAll("delete-button", "icon-button");

                btnEliminar.setOnAction(e -> {
                    Path item = getItem();
                    if (item != null) {
                        directorySecurity.removeFromWhitelist(item);
                        listPaths.getItems().remove(item);
                    }
                });

                content.getChildren().addAll(lblPath, spacer, btnEliminar);
                content.getStyleClass().add("path-row");
            }

            @Override
            protected void updateItem(Path item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    lblPath.setText(item.toString());
                    setGraphic(content);
                }
            }
        });

        TextField txtNuevoPath = new TextField();
        txtNuevoPath.setPromptText(I18nManager.get("IngresePath"));
        txtNuevoPath.setPrefWidth(400);

        Button btnAgregarPath = new Button(I18nManager.get("Agregar"));
        Button btnElegirCarpeta = new Button("📁");
        btnElegirCarpeta.setTooltip(new Tooltip(I18nManager.get("ElegirCarpeta")));
        btnElegirCarpeta.setCursor(javafx.scene.Cursor.HAND);
        btnElegirCarpeta.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(I18nManager.get("SeleccionarCarpeta"));

            Window window = txtNuevoPath.getScene().getWindow();

            File selectedDir = chooser.showDialog(window);
            if (selectedDir != null) {
                txtNuevoPath.setText(selectedDir.getAbsolutePath());
                btnAgregarPath.fire();
            }
        });
        btnAgregarPath.setCursor(javafx.scene.Cursor.HAND);
        btnAgregarPath.setOnAction(e -> {
            if (!txtNuevoPath.getText().isBlank()) {
                Path dir = Paths.get(txtNuevoPath.getText());
                directorySecurity.addToWhitelist(dir);
                listPaths.getItems().add(dir);
                txtNuevoPath.clear();
            }
        });

        HBox agregarPathBox = new HBox(8, txtNuevoPath, btnElegirCarpeta, btnAgregarPath);
        agregarPathBox.setStyle("-fx-alignment: center-left;");
        content.getChildren().addAll(labelOpciones, listPaths, agregarPathBox);
        content.setPadding(new Insets(5));
        return content;
    }

    private VBox blackList(){

        Label labelOpciones = new Label();
        labelOpciones.setText(I18nManager.get("BlackList"));

        VBox content = new VBox(3);
        ListView<Path> listPaths = new ListView<>();
        listPaths.getItems().addAll(directorySecurity.getBlacklist());

        listPaths.setCellFactory(lv -> new ListCell<Path>() {

            private final Label lblPath = new Label();
            private final Button btnEliminar = new Button("✖");
            private final Region spacer = new Region();
            private final HBox content = new HBox(10);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);

                btnEliminar.getStyleClass().addAll("delete-button", "icon-button");

                btnEliminar.setOnAction(e -> {
                    Path item = getItem();
                    if (item != null) {
                        directorySecurity.removeFromBlacklist(item);
                        listPaths.getItems().remove(item);
                    }
                });

                content.getChildren().addAll(lblPath, spacer, btnEliminar);
                content.getStyleClass().add("path-row");
            }

            @Override
            protected void updateItem(Path item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    lblPath.setText(item.toString());
                    setGraphic(content);
                }
            }
        });

        TextField txtNuevoPath = new TextField();
        txtNuevoPath.setPromptText(I18nManager.get("IngresePath"));
        txtNuevoPath.setPrefWidth(400);

        Button btnAgregarPath = new Button(I18nManager.get("Agregar"));
        Button btnElegirCarpeta = new Button("📁");
        btnElegirCarpeta.setTooltip(new Tooltip(I18nManager.get("ElegirCarpeta")));
        btnElegirCarpeta.setCursor(javafx.scene.Cursor.HAND);
        btnElegirCarpeta.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(I18nManager.get("SeleccionarCarpeta"));

            Window window = txtNuevoPath.getScene().getWindow();

            File selectedDir = chooser.showDialog(window);
            if (selectedDir != null) {
                txtNuevoPath.setText(selectedDir.getAbsolutePath());
                btnAgregarPath.fire();
            }
        });
        btnAgregarPath.setCursor(javafx.scene.Cursor.HAND);
        btnAgregarPath.setOnAction(e -> {
            if (!txtNuevoPath.getText().isBlank()) {
                Path dir = Paths.get(txtNuevoPath.getText());
                directorySecurity.addToBlacklist(dir);
                listPaths.getItems().add(dir);
                txtNuevoPath.clear();
            }
        });

        HBox agregarPathBox = new HBox(8, txtNuevoPath, btnElegirCarpeta, btnAgregarPath);
        agregarPathBox.setStyle("-fx-alignment: center-left;");
        content.getChildren().addAll(labelOpciones, listPaths, agregarPathBox);
        content.setPadding(new Insets(5));

        return content;
    }
    private String whiteListOverridesBlackListText(){
        return I18nManager.get(directorySecurity.isWhitelistOverridesBlackList() ? "WhitelistOverridesBlacklistTrueText" : "WhitelistOverridesBlacklistFalseText");
    }
    public HBox botonOverride(){
        return Buttons.checkBox(I18nManager.get("OverrideCheckbox"), this::whiteListOverridesBlackListText, directorySecurity::isWhitelistOverridesBlackList, directorySecurity::setWhitelistOverridesBlackList);
    }
    public HBox botonDeleteFiles(){
        return Buttons.checkBox(I18nManager.get("DeleteFilesCheckbox"), () -> I18nManager.get(directorySecurity.isDeleteFilesNotInCheckpoint() == true ? "DeleteFilesNotInCheckpointTrueText" : "DeleteFilesNotInCheckpointFalseText"), directorySecurity::isDeleteFilesNotInCheckpoint, directorySecurity::setDeleteFilesNotInCheckpoint);
    }
    public HBox botonMaximoTamañoArchivo(){
        return Buttons.fileSizeField(I18nManager.get("TamañoMaximoDeCheckpoint"), directorySecurity::getMaxCheckpointSizeInBytes, directorySecurity::setMaxCheckpointSizeInBytes);
    }

    @Override
    public String getName() {
        return I18nManager.get("Configuracion");
    }
}
