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
import utils.I18nManager;

import java.io.File;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
        Label superTitulo = new Label(I18nManager.get("GestionarPartida"));
        superTitulo.getStyleClass().add("inicio-title");

        Label titulo = new Label(
                I18nManager.get("Juego") + ": " + partida.getJuego().getTitulo() +
                        " \n"+I18nManager.get("Partida")+": " + partida.getTitulo()
        );
        titulo.getStyleClass().add("inicio-subtitle");
        titulo.setTextAlignment(TextAlignment.CENTER);
        titulo.setAlignment(Pos.CENTER);
        titulo.setWrapText(true);



        checkpointList = new ListView<>();
        checkpointList.setCellFactory(lv -> new ListCell<>(){

            private final Label nombreLabel = new Label();
            private final Label fechaLabel = new Label();
            private final Label descripcionLabel = new Label();
            private final Region espacio = new Region();
            private final HBox nombreHBox = new HBox(1, fechaLabel, espacio, nombreLabel);
            private final VBox box = new VBox(2, nombreHBox, descripcionLabel);
            private final HBox content = new HBox(10, box);

            {
                nombreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                fechaLabel.setStyle(" -fx-font-size: 14px;");
                HBox.setHgrow(espacio, Priority.ALWAYS);
                descripcionLabel.setStyle("-fx-text-fill: gray; -fx-font-weight: bold; -fx-font-size: 11px;");
                descripcionLabel.setWrapText(true);
                descripcionLabel.setMaxWidth(Double.MAX_VALUE);
                setPrefWidth(USE_COMPUTED_SIZE);

                box.setFillWidth(true);
                nombreHBox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(nombreHBox, Priority.ALWAYS);


                box.setPadding(new Insets(5));
                box.setMaxWidth(Double.MAX_VALUE);
                VBox.setVgrow(descripcionLabel, Priority.ALWAYS);
                //descripcionLabel.maxWidthProperty().bind(box.widthProperty().subtract(20));
                descripcionLabel.setWrapText(true);
                descripcionLabel.prefWidthProperty().bind(
                        content.widthProperty()
                );
                HBox.setHgrow(content, Priority.ALWAYS);
                content.prefWidthProperty().bind(widthProperty().subtract(30));

            }
            @Override
            protected void updateItem(Checkpoint item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    nombreLabel.setText(item.nombreLabelText());
                    descripcionLabel.setText(item.descripcionLabelText());
                    fechaLabel.setText(item.fechaLabelText());
                    setGraphic(content);
                    setText(null);
                }
            }
        } );
        refrescarCheckpoints();

        Button guardarBtn = new Button(I18nManager.get("GuardarCheckpoint"));
        guardarBtn.setOnAction(e -> guardarCheckpoint());

        Button cargarUltimoBtn = new Button(I18nManager.get("CargarUltimoCheckpoint"));
        cargarUltimoBtn.setOnAction(e -> cargarUltimoCheckpoint());

        Button cargarBtn = new Button(I18nManager.get("CargarSeleccionado"));
        cargarBtn.setOnAction(e -> cargarCheckpointSeleccionado());

        Button eliminarBtn = new Button(I18nManager.get("EliminarSeleccionado"));
        eliminarBtn.setOnAction(e -> eliminarCheckpointSeleccionado());

        Button exportarSeleccionado = new Button(I18nManager.get("ExportarSeleccionado"));
        exportarSeleccionado.setOnAction(e -> exportarCheckpointSeleccionado());

        Button rotacion = new Button(I18nManager.get("ModificarRotacion"));
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
        if(!partidaService.isPartidaActualCargada(partida) && !Dialogs.confirmar(I18nManager.get("ConfirmarGuardarOtraPartidaCargada", partida.getJuego().getPartidaActual().getTitulo())))
            return;
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(I18nManager.get("GuardarCheckpoint"));
        dialog.setHeaderText(I18nManager.get("IngresarDatosCheckpoint"));

        ButtonType aceptarBtn = new ButtonType(I18nManager.get("Guardar"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelarBtn = new ButtonType(I18nManager.get("Cancelar"), ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(aceptarBtn, cancelarBtn);

        TextField nombreField = new TextField();
        nombreField.setPromptText(I18nManager.get("NombreOpcional"));

        TextArea descripcionField = new TextArea();
        descripcionField.setPromptText(I18nManager.get("DescripcionOpcional"));
        descripcionField.setPrefRowCount(3);
        descripcionField.setWrapText(true);
        descripcionField.setMaxWidth(400);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label(I18nManager.get("Nombre") + ":"), 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(new Label(I18nManager.get("Descripción") + ":"), 0, 1);
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
        if(Dialogs.confirmar(I18nManager.get("ConfirmarCargarCheckpoint", checkpoint.getStringReferencia()))){
            if (!partidaService.isPartidaActualCargada(partida) && Dialogs.confirmar(I18nManager.get("GuardarPartidaAntesDeCargar", partida.getJuego().getPartidaActual().getTitulo()))) {
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
        fileChooser.setTitle(I18nManager.get("ExportarCheckpoint"));
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

        if (Dialogs.confirmar(I18nManager.get("ConfirmarBorrarCheckpoint"))) {
            checkpointService.eliminarCheckpoint(seleccionado);
            refrescarCheckpoints();
        }
    }
    private void verMenuEstrategiaCheckpoints() {
        Dialog<CheckpointStrategy> dialog = new Dialog<>();
        dialog.setTitle(I18nManager.get("ConfiguracionDeCheckpoints"));
        dialog.getDialogPane().setMinWidth(500);
        dialog.getDialogPane().setMinHeight(400);
        dialog.getDialogPane().setStyle("-fx-background-color: #1e1e2e;");
        dialog.getDialogPane().setPadding(new Insets(20));

        ButtonType okButtonType = new ButtonType(I18nManager.get("Guardar"), ButtonBar.ButtonData.OK_DONE);
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
                Label lbl = new Label(I18nManager.get("LimiteDeCheckpoints"));
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

                Label l1 = new Label(I18nManager.get("CheckpoiintsRecientesSeguros")); l1.setStyle(labelStyle);
                Label l2 = new Label(I18nManager.get("CapacidadMaximaTotal")); l2.setStyle(labelStyle);

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

        Label tituloSecundario = new Label(I18nManager.get("EstrategiaDeRotacion"));
        tituloSecundario.setStyle("-fx-text-fill: #8888aa; -fx-font-weight: bold;");

        VBox layoutPrincipal = new VBox(10, tituloSecundario, comboBox, camposDinamicos);
        dialog.getDialogPane().setContent(layoutPrincipal);
        comboBox.getOnAction().handle(null);
        Optional<CheckpointStrategy> strategyOptional;
        do{
            strategyOptional = dialog.showAndWait();
            if(strategyOptional.isPresent() && Dialogs.confirmar(I18nManager.get("ConfirmarEstrategiaRotacion"))){
                partidaService.modificarCheckpointStrategy(partida, strategyOptional.get());
                return;
            }
        }while (strategyOptional.isPresent());
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
