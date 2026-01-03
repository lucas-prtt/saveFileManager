package ui.tabWrapper;

import domain.Archivos.juego.Directorio;
import domain.Juegos.Juego;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import servicios.JuegosService;
import ui.MainController;
import utils.Dialogs;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TabElegirJuego extends TabWrapper{
    private JuegosService juegosService;

    public String getName(){
        return "Elegir Juego";
    }

    @Override
    public void init(MainController mainController) {
        super.init(mainController);
        this.juegosService = mainController.getJuegosService();
    }

    public VBox getContent() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label label = new Label("Elegir Juego");
        label.getStyleClass().add("inicio-title");


        List<Juego> juegos = juegosService.getJuegos();
        ListView<Juego> listJuegos = new ListView<>(FXCollections.observableArrayList(juegos));


        listJuegos.setCellFactory(lv -> new ListCell<Juego>() {

            private final Label labelJuego = new Label();
            private final HBox content = new HBox(10);
            {
                HBox.setHgrow(content, Priority.ALWAYS);
                content.getChildren().addAll(labelJuego);
                content.getStyleClass().add("path-row");
            }
            @Override
            protected void updateItem(Juego item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    labelJuego.setText(item.getTitulo());
                    setGraphic(content);
                }
            }
        });
        listJuegos.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Juego seleccionado = listJuegos.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    controller.createOrSelectIf(
                            TabGestionarJuego.class,
                            (tabGestionarJuego -> tabGestionarJuego.getJuego().equals(seleccionado)),
                            () -> new TabGestionarJuego(seleccionado)
                    );
                }
            }
        });
        Button btnEliminar = new Button("Eliminar juego");
        btnEliminar.setOnAction(e -> {
            if(listJuegos.getSelectionModel().getSelectedItem() == null){
                controller.showToast("Seleccione un juego primero");
                return;
            }

                if(Dialogs.confirmarDoble("Esta seguro que desea eliminar " + listJuegos.getSelectionModel().getSelectedItem().getTitulo()+ "? ", listJuegos.getSelectionModel().getSelectedItem().getTitulo())) {
                juegosService.eliminarJuego(listJuegos.getSelectionModel().getSelectedItem());
                update();
                System.out.println("Se borro el juego" + listJuegos.getSelectionModel().getSelectedItem().getTitulo() + " - " + listJuegos.getSelectionModel().getSelectedItem().getId());
            }//TODO: Cerrar tabs de partidas y checkpoints del juego
        });

        Button btnEditar = new Button("Editar juego");
        btnEditar.setOnAction(e -> {
            editarJuego(listJuegos.getSelectionModel().getSelectedItem());
        });
        HBox botones = new HBox(10);
        botones.setPadding(new Insets(10));
        botones.setAlignment(Pos.CENTER);

        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.findTab(TabInicial.class).ifPresent(TabWrapper::focus));
        botones.getChildren().addAll(  btnIrInicio, btnEliminar, btnEditar);
        content.getChildren().addAll(label, listJuegos, botones);
        return content;
    }
    private class JuegoEditDTO {
        String id;
        String titulo;
        List<Directorio> directorios;
    }
    public void editarJuego(Juego juegoExistente) {
        if(juegoExistente == null){
            controller.showToast("Seleccione un juego ya registrado");
            return;
        }
        AtomicReference<Boolean> eliminaDirectorios = new AtomicReference<>(false);
        JuegoEditDTO juegoEditDTO = new JuegoEditDTO();
        juegoEditDTO.titulo = juegoExistente.getTitulo();
        juegoEditDTO.directorios = new ArrayList<>(juegoExistente.getSaveFilePaths());
        juegoEditDTO.id = juegoExistente.getId();

        Dialog<JuegoEditDTO> dialog = new Dialog<>();
        dialog.setTitle("Editar Juego");
        dialog.setHeaderText("Editar juego: " + juegoEditDTO.titulo);

        ButtonType confirmarBtnType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarBtnType, ButtonType.CANCEL);

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        Label lblTitulo = new Label("Título del juego:");
        TextField txtTitulo = new TextField(juegoEditDTO.titulo);
        txtTitulo.textProperty().addListener((obs, oldVal, newVal) -> juegoEditDTO.titulo = newVal);

        Label lblPaths = new Label("Paths de guardado:");
        ListView<Directorio> listPaths = new ListView<>();
        listPaths.getItems().addAll(juegoEditDTO.directorios);

        listPaths.setCellFactory(lv -> new ListCell<>() {
            private final Label lblPath = new Label();
            private final Button btnEliminar = new Button("✖");
            private final Region spacer = new Region();
            private final HBox content = new HBox(10);

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                btnEliminar.getStyleClass().addAll("delete-button", "icon-button");
                btnEliminar.setOnAction(e -> {
                    Directorio item = getItem();
                    eliminaDirectorios.set(Boolean.TRUE);
                    if (item != null) {
                        juegoEditDTO.directorios.remove(item);
                        listPaths.getItems().remove(item);
                    }
                });
                content.getChildren().addAll(lblPath, spacer, btnEliminar);
                content.getStyleClass().add("path-row");
            }

            @Override
            protected void updateItem(Directorio item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || item == null ? null : content);
                if (item != null) lblPath.setText(item.getPathPrincipal().toString());
            }
        });

        /* Agregar paths */
        TextField txtNuevoPath = new TextField();
        txtNuevoPath.setPromptText("Ingrese un path");
        txtNuevoPath.setPrefWidth(400);

        Button btnElegirCarpeta = new Button("📁");
        Button btnElegirArchivo = new Button("📄");
        Button btnAgregarPath = new Button("Agregar");

        btnElegirCarpeta.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Seleccionar carpeta de guardado");
            File selectedDir = chooser.showDialog(txtTitulo.getScene().getWindow());
            if (selectedDir != null) {
                txtNuevoPath.setText(selectedDir.getAbsolutePath());
                btnAgregarPath.fire();
            }
        });

        btnElegirArchivo.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Seleccionar archivo de guardado");
            File selectedFile = chooser.showOpenDialog(txtTitulo.getScene().getWindow());
            if (selectedFile != null) {
                txtNuevoPath.setText(selectedFile.getAbsolutePath());
                btnAgregarPath.fire();
            }
        });

        btnAgregarPath.setOnAction(e -> {
            if (!txtNuevoPath.getText().isBlank()) {
                Directorio dir = new Directorio(Path.of(txtNuevoPath.getText()));
                juegoEditDTO.directorios.add(dir);
                listPaths.getItems().add(dir);
                txtNuevoPath.clear();
            }
        });

        HBox agregarPathBox = new HBox(8, txtNuevoPath, btnElegirCarpeta, btnElegirArchivo, btnAgregarPath);
        agregarPathBox.setStyle("-fx-alignment: center-left;");

        root.getChildren().addAll(lblTitulo, txtTitulo, lblPaths, listPaths, agregarPathBox);
        dialog.getDialogPane().setContent(root);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarBtnType) return juegoEditDTO;
            return null;
        });

        dialog.showAndWait().ifPresent(j -> {
            if( eliminaDirectorios.get() && !Dialogs.confirmar("Esta seguro? Si elimina directorios, tambien se eliminaran los archivos guardados en los mismos dentro de cada checkpoint."))
                return;
            try {
                controller.getJuegosService().modificarJuego(j.id, j.directorios, j.titulo);
            } catch (Exception ex) {
                ex.printStackTrace();
                controller.showToast("Error al guardar el juego: " + ex.getMessage());
            }
        });
        update();
    }

}

