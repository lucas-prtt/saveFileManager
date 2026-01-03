package ui.tabWrapper;

import domain.Archivos.juego.Directorio;
import domain.Juegos.Juego;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import servicios.JuegosService;
import ui.MainController;
import utils.Dialogs;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class TabCargarJuego extends TabWrapper {

    private Juego juego;

    private JuegosService juegosService;

    private final String TITULODEFAULT = "Nombre del juego";

    public String getName(){
        return "Cargar Juego";
    }
    @Override
    public void init(MainController controller){
        super.init(controller);
        juegosService = controller.getJuegosService();
    }

    @Override
    public VBox getContent() {

        juego = new Juego();
        juego.setTitulo("Nombre del juego");
        juego.setSaveFilePaths(new ArrayList<>());

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20;");

        /*       Titulo      */
        Label lblTitulo = new Label("Título del juego:");
        TextField txtTitulo = new TextField(juego.getTitulo());

        txtTitulo.textProperty().addListener((obs, oldVal, newVal) -> {
            juego.setTitulo(newVal);
        });

        /*       Lista de paths       */
        Label lblPaths = new Label("Paths de guardado:");

        ListView<Directorio> listPaths = new ListView<>();
        listPaths.getItems().addAll(juego.getSaveFilePaths());

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
                    if (item != null) {
                        juego.eliminarDirectorio(item);
                        listPaths.getItems().remove(item);
                    }
                });

                content.getChildren().addAll(lblPath, spacer, btnEliminar);
                content.getStyleClass().add("path-row");
            }

            @Override
            protected void updateItem(Directorio item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    lblPath.setText(item.getPathPrincipal().toString());
                    setGraphic(content);
                }
            }
        });

        /*      Agregar Paths       */
        TextField txtNuevoPath = new TextField();
        txtNuevoPath.setPromptText("Ingrese un path");
        txtNuevoPath.setPrefWidth(400);

        Button btnElegirCarpeta = new Button("📁");
        Button btnAgregarPath = new Button("Agregar");
        btnElegirCarpeta.setTooltip(new Tooltip("Elegir carpeta"));
        btnElegirCarpeta.setCursor(javafx.scene.Cursor.HAND);
        btnElegirCarpeta.setOnAction(e -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Seleccionar carpeta de guardado");

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
                Directorio dir = new Directorio(Path.of(txtNuevoPath.getText()));
                juego.agregarDirectorio(dir);
                listPaths.getItems().add(dir);
                txtNuevoPath.clear();
            }
        });

        HBox agregarPathBox = new HBox(8, txtNuevoPath, btnElegirCarpeta, btnAgregarPath);
        agregarPathBox.setStyle("-fx-alignment: center-left;");

        /*      Confirmar/Cancelar       */
        Button btnConfirmar = new Button("Confirmar");
        btnConfirmar.setOnAction(e -> {
            if(juego.getTitulo().equals(TITULODEFAULT) && !Dialogs.confirmar("Esta seguro que desea guardar el juego con el titulo " + TITULODEFAULT + "?"))
                return;
            if(juego.getSaveFilePaths().isEmpty() && !Dialogs.confirmar("Esta seguro que desea guardar un juego sin directorios de guardado?"))
                return;
            try {
                juegosService.guardarJuego(juego);
                controller.findTab(TabElegirJuego.class).ifPresent(TabWrapper::update);
                controller.selectTab(controller.findTab(TabInicial.class).orElseThrow());
                close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setOnAction(e -> controller.findTab(TabInicial.class).ifPresent(TabWrapper::focus));

        HBox botones = new HBox(10, btnConfirmar, btnCancelar);

        root.getChildren().addAll(
                lblTitulo,
                txtTitulo,
                lblPaths,
                listPaths,
                agregarPathBox,
                botones
        );

        return root;
    }

}

