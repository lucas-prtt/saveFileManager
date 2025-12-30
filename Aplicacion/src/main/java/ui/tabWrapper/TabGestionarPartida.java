package ui.tabWrapper;

import domain.Juegos.Checkpoint;
import domain.Juegos.Partida;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import servicios.CheckpointService;
import servicios.PartidaService;
import ui.MainController;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
        Label titulo = new Label(
                "Juego: " + partida.getJuego().getTitulo() +
                        " | Partida: " + partida.getTitulo()
        );

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

        HBox acciones = new HBox(10, guardarBtn, cargarUltimoBtn, cargarBtn, eliminarBtn);

        return new VBox(10, titulo, checkpointList, acciones);
    }
    private void refrescarCheckpoints() {
        checkpointList.getItems().setAll(
                partidaService.obtenerCheckpoints(partida)
        );
    }

    private void guardarCheckpoint() {
        if(!partidaService.isPartidaActualCargada(partida) && !confirmar("Actualmente hay otra partida cargada ( " +partida.getJuego().getPartidaActual().getTitulo()+ " ) . Esta seguro que desea guardar aqui?"))
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
            }
            return null;
        });

        dialog.showAndWait();
    }
    private void cargarUltimoCheckpoint() {
        if(checkpointList.getItems().isEmpty())
            return;
        Checkpoint checkpoint = checkpointList.getItems().getLast();
        cargarCheckpointConVerificaciones(checkpoint);
    }
    private void cargarCheckpointConVerificaciones(Checkpoint checkpoint){
        if(confirmar("Esta seguro que desea cargar este checkpoint ("+checkpoint.getStringReferencia()+") ?")){
            if (!partidaService.isPartidaActualCargada(partida) && confirmar("Actualmente hay otra partida cargada en el sistema ( " + partida.getJuego().getPartidaActual().getTitulo() + " ). Quiere guardar su proceso allí antes de cargar esta?")) {
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
    private void eliminarCheckpointSeleccionado() {
        Checkpoint seleccionado = checkpointList.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        if (confirmar("¿Desea eliminar este checkpoint?")) {
            checkpointService.eliminarCheckpoint(seleccionado);
            refrescarCheckpoints();
        }
    }
    private boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(mensaje);

        return alert.showAndWait()
                .filter(r -> r == ButtonType.OK)
                .isPresent();
    }
    @Override
    public String getName() {
        return partida.getJuego().getTitulo() + "  -  " +  partida.getTitulo();
    }
}
