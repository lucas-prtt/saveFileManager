package ui.tabWrapper;

import domain.Juegos.Checkpoint;
import domain.Juegos.CheckpointStrategys.CheckpointStrategy;
import domain.Juegos.CheckpointStrategys.FIFOMaxCheckpointStrategy;
import domain.Juegos.CheckpointStrategys.RandomChanceCheckpointStrategy;
import domain.Juegos.CheckpointStrategys.SaveAllCheckpointsStrategy;
import domain.Juegos.Partida;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.StringConverter;
import lombok.Getter;
import servicios.CheckpointService;
import servicios.PartidaService;
import ui.MainController;
import utils.Dialogs;

import java.io.File;
import java.nio.file.Path;

public class TabGestionarPartida extends TabWrapper{
    private CheckpointService checkpointService;
    private PartidaService partidaService;
    @Getter
    private Partida partida;
    private ListView<Checkpoint> checkpointList;

    @Override
    public void init(MainController mainController) {
        super.init(mainController);
        this.partidaService = controller.getPartidaService();
        this.checkpointService = controller.getCheckpointService();
    }

    public TabGestionarPartida(Partida partida){
        super();
        this.partida = partida;
    }

    @Override
    public VBox getContent() {
        Label superTitulo = new Label("Gestionar partida");
        superTitulo.getStyleClass().add("inicio-title");

        Label titulo = new Label(
                "Juego: " + partida.getJuego().getTitulo() +
                        " \n Partida: " + partida.getTitulo()
        );
        titulo.getStyleClass().add("inicio-subtitle");
        titulo.setTextAlignment(TextAlignment.CENTER);
        titulo.setAlignment(Pos.CENTER);
        titulo.setWrapText(true);



        checkpointList = new ListView<>();
        checkpointList.setCellFactory(lv -> new ListCell<>(){

            private final HBox content = new HBox(10);
            {
                HBox.setHgrow(content, Priority.ALWAYS);
            }
            @Override
            protected void updateItem(Checkpoint item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item.listRepresentation());
                }
            }
        } );
        refrescarCheckpoints();

        Button guardarBtn = new Button("Guardar checkpoint");
        guardarBtn.setOnAction(e -> guardarCheckpoint());

        Button cargarUltimoBtn = new Button("Cargar último checkpoint");
        cargarUltimoBtn.setOnAction(e -> cargarUltimoCheckpoint());

        Button cargarBtn = new Button("Cargar seleccionado");
        cargarBtn.setOnAction(e -> cargarCheckpointSeleccionado());

        Button eliminarBtn = new Button("Eliminar seleccionado");
        eliminarBtn.setOnAction(e -> eliminarCheckpointSeleccionado());

        Button exportarSeleccionado = new Button("Exportar seleccionado");
        exportarSeleccionado.setOnAction(e -> exportarCheckpointSeleccionado());

        Button rotacion = new Button("Modificar algoritmo de rotación");
        rotacion.setOnAction(e -> verMenuEstrategiaCheckpoints());

        HBox acciones = new HBox(10, guardarBtn, cargarUltimoBtn, cargarBtn, eliminarBtn, exportarSeleccionado, rotacion);

        VBox content = new VBox(10,superTitulo, titulo, checkpointList, acciones);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        return content;
    }
    private void refrescarCheckpoints() {
        checkpointList.getItems().setAll(
                partidaService.obtenerCheckpoints(partida).reversed()
        );
        checkpointList.requestLayout();
    }

    private void guardarCheckpoint() {
        if(!partidaService.isPartidaActualCargada(partida) && !Dialogs.confirmar("Actualmente hay otra partida cargada ( " +partida.getJuego().getPartidaActual().getTitulo()+ " ) . Esta seguro que desea guardar aqui?"))
            return;
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Guardar checkpoint");
        dialog.setHeaderText("Ingrese los datos del checkpoint");

        ButtonType aceptarBtn = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelarBtn = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(aceptarBtn, cancelarBtn);

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre (opcional)");

        TextArea descripcionField = new TextArea();
        descripcionField.setPromptText("Descripción (opcional)");
        descripcionField.setPrefRowCount(3);
        descripcionField.setWrapText(true);
        descripcionField.setMaxWidth(400);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(descripcionField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == aceptarBtn) {
                String nombre = nombreField.getText().isBlank()
                        ? null
                        : nombreField.getText();

                String descripcion = descripcionField.getText().isBlank()
                        ? null
                        : descripcionField.getText();

                checkpointService.guardarCheckpoint(partida, nombre, descripcion);
                refrescarCheckpoints();
                checkpointList.scrollTo(0);
                checkpointList.getSelectionModel().select(0);
            }
            return null;
        });

        dialog.showAndWait();
    }
    private void cargarUltimoCheckpoint() {
        if(checkpointList.getItems().isEmpty())
            return;
        Checkpoint checkpoint = checkpointList.getItems().getFirst();
        cargarCheckpointConVerificaciones(checkpoint);
    }
    private void cargarCheckpointConVerificaciones(Checkpoint checkpoint){
        if(Dialogs.confirmar("Esta seguro que desea cargar este checkpoint ("+checkpoint.getStringReferencia()+") ?")){
            if (!partidaService.isPartidaActualCargada(partida) && Dialogs.confirmar("Actualmente hay otra partida cargada en el sistema ( " + partida.getJuego().getPartidaActual().getTitulo() + " ). Quiere guardar su proceso allí antes de cargar esta?")) {
                checkpointService.guardarCheckpointUltimaPartida(partida);
            }
            checkpointService.cargarCheckpoint(checkpoint);
        }
    }
    private void cargarCheckpointSeleccionado() {
        Checkpoint seleccionado = checkpointList.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;
        cargarCheckpointConVerificaciones(seleccionado);
    }
    private void exportarCheckpointSeleccionado() {
        Checkpoint seleccionado = checkpointList.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar checkpoint");
        fileChooser.setInitialFileName(
                "Export.zip"
        );


        Window window = checkpointList.getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);
        if (file == null) return;
        Path path = file.toPath();
        checkpointService.exportarCheckpoint(path, seleccionado);
    }

    private void eliminarCheckpointSeleccionado() {
        Checkpoint seleccionado = checkpointList.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        if (Dialogs.confirmar("¿Desea eliminar este checkpoint?")) {
            checkpointService.eliminarCheckpoint(seleccionado);
            refrescarCheckpoints();
        }
    }
    private void verMenuEstrategiaCheckpoints() {
        Dialog<CheckpointStrategy> dialog = new Dialog<>();
        dialog.setTitle("Configuración de Checkpoints");
        dialog.getDialogPane().setMinWidth(500);
        dialog.getDialogPane().setMinHeight(400);
        dialog.getDialogPane().setStyle("-fx-background-color: #1e1e2e;");
        dialog.getDialogPane().setPadding(new Insets(20));

        ButtonType okButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        CheckpointStrategy estrategiaActual = partida.getCheckpointStrategy();
        ComboBox<CheckpointStrategy> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(new SaveAllCheckpointsStrategy(), new FIFOMaxCheckpointStrategy(), new RandomChanceCheckpointStrategy());

        comboBox.setStyle("-fx-background-color: #2f2f46; -fx-border-color: #4a6cff; -fx-border-radius: 5; -fx-text-fill: white; -fx-min-height: 45px;");
        comboBox.setMaxWidth(Double.MAX_VALUE);

        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CheckpointStrategy item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); }
                else {
                    Label n = new Label(item.nombre()); n.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                    Label d = new Label(item.descripcion()); d.setStyle("-fx-text-fill: #a0a0b8; -fx-font-size: 11px;");
                    setGraphic(new VBox(2, n, d));
                }
                setStyle("-fx-background-color: #2f2f46; -fx-border-color: #3f3f5a; -fx-border-width: 0 0 1 0;");
            }
        });
        comboBox.setButtonCell(comboBox.getCellFactory().call(null));

        VBox camposDinamicos = new VBox(15);
        String labelStyle = "-fx-text-fill: #4a6cff; -fx-font-weight: bold; -fx-font-size: 11px;";

        comboBox.setOnAction(e -> {
            camposDinamicos.getChildren().clear();
            CheckpointStrategy selected = comboBox.getValue();

            if (selected instanceof FIFOMaxCheckpointStrategy fifoSelected) {
                int val = (estrategiaActual instanceof FIFOMaxCheckpointStrategy f) ? f.getMaxCheckpoints() : fifoSelected.getMaxCheckpoints();

                Spinner<Integer> sp = crearSpinnerEditable(1, 1000, val);
                Label lbl = new Label("LÍMITE DE CHECKPOINTS:");
                lbl.setStyle(labelStyle);

                camposDinamicos.getChildren().add(new VBox(5, lbl, sp));

                dialog.setResultConverter(btn -> {
                    if (btn == okButtonType) {
                        commitSpinnerValue(sp);
                        fifoSelected.setMaxCheckpoints(sp.getValue());
                        return fifoSelected;
                    }
                    return null;
                });

            } else if (selected instanceof RandomChanceCheckpointStrategy randSelected) {
                int valMax = (estrategiaActual instanceof RandomChanceCheckpointStrategy r) ? r.getMaxTailSize() : randSelected.getMaxTailSize();
                int valSafe = (estrategiaActual instanceof RandomChanceCheckpointStrategy r) ? r.getAllCheckpointsTailSize() : randSelected.getAllCheckpointsTailSize();

                Spinner<Integer> spMax = crearSpinnerEditable(5, 1000, valMax);
                Spinner<Integer> spSafe = crearSpinnerEditable(2, 1000, valSafe);

                Label l1 = new Label("CHECKPOINTS RECIENTES (SEGUROS):"); l1.setStyle(labelStyle);
                Label l2 = new Label("CAPACIDAD MÁXIMA TOTAL:"); l2.setStyle(labelStyle);

                camposDinamicos.getChildren().addAll(new VBox(5, l1, spSafe), new VBox(5, l2, spMax));

                dialog.setResultConverter(btn -> {
                    if (btn == okButtonType) {
                        commitSpinnerValue(spMax);
                        commitSpinnerValue(spSafe);
                        randSelected.setMaxTailSize(spMax.getValue());
                        randSelected.setAllCheckpointsTailSize(spSafe.getValue());
                        return randSelected;
                    }
                    return null;
                });
            } else {
                dialog.setResultConverter(btn -> btn == okButtonType ? selected : null);
            }
        });

        comboBox.getItems().stream()
                .filter(i -> i.getClass().equals(estrategiaActual.getClass()))
                .findFirst().ifPresent(comboBox::setValue);

        Label tituloSecundario = new Label("ESTRATEGIA DE ROTACIÓN");
        tituloSecundario.setStyle("-fx-text-fill: #8888aa; -fx-font-weight: bold;");

        VBox layoutPrincipal = new VBox(10, tituloSecundario, comboBox, camposDinamicos);
        dialog.getDialogPane().setContent(layoutPrincipal);
        comboBox.getOnAction().handle(null);

        dialog.showAndWait().ifPresent(strategy -> partidaService.modificarCheckpointStrategy(partida, strategy));
    }

    private Spinner<Integer> crearSpinnerEditable(int min, int max, int init) {
        Spinner<Integer> spinner = new Spinner<>(min, max, init);
        spinner.setEditable(true);
        spinner.setMaxWidth(Double.MAX_VALUE);
        spinner.setStyle("-fx-background-color: #2f2f46; -fx-border-color: #4a6cff; -fx-border-radius: 5;");
        spinner.getEditor().setStyle("-fx-background-color: #2f2f46; -fx-text-fill: white; -fx-font-size: 14px;");

        spinner.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) commitSpinnerValue(spinner);
        });
        return spinner;
    }

    private <T> void commitSpinnerValue(Spinner<T> spinner) {
        if (!spinner.isEditable()) return;
        String text = spinner.getEditor().getText();
        SpinnerValueFactory<T> valueFactory = spinner.getValueFactory();
        if (valueFactory != null) {
            StringConverter<T> converter = valueFactory.getConverter();
            if (converter != null) {
                try {
                    T value = converter.fromString(text);
                    valueFactory.setValue(value);
                } catch (Exception e) {
                    spinner.getEditor().setText(converter.toString(valueFactory.getValue()));
                }
            }
        }
    }

    @Override
    public String getName() {
        return partida.getJuego().getTitulo() + "  -  " +  partida.getTitulo();
    }
}
