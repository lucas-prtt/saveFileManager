package ui.tabWrapper;

import domain.Juegos.Juego;
import domain.Juegos.Partida;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;
import servicios.JuegosService;
import servicios.PartidaService;
import ui.MainController;
import utils.Dialogs;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TabGestionarJuego extends TabWrapper {
    @Getter
    private Juego juego;
    private JuegosService juegosService;
    private PartidaService partidaService;
    public String getName(){
        return juego.getTitulo();
    }

    public TabGestionarJuego(Juego juego){
        super();
        this.juego = juego;
    }

    @Override
    public void init(MainController mainController) {
        super.init(mainController);
        juegosService = mainController.getJuegosService();
        partidaService = mainController.getPartidaService();
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root-padding");

        Label lblTitulo = new Label("Gestionar juego");
        lblTitulo.getStyleClass().add("inicio-title");

        Label lblNombre = new Label("Juego: " + juego.getTitulo());
        lblNombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");




        List<Partida> partidas = juegosService.getPartidas(juego);
        ListView<Partida> partidaListView = new ListView<>(FXCollections.observableArrayList(partidas));
        partidaListView.setCellFactory(lv -> new ListCell<Partida>() {

            private final Label labelPartida = new Label();
            private final HBox content = new HBox(10);
            {
                HBox.setHgrow(content, Priority.ALWAYS);
                content.getChildren().addAll(labelPartida);
                content.getStyleClass().add("path-row");
            }
            @Override
            protected void updateItem(Partida item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    labelPartida.setText(item.getTitulo() + "  -  " + item.getId().substring(0, 10));
                    setGraphic(content);
                }
            }
        });
        partidaListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Partida seleccionada = partidaListView.getSelectionModel().getSelectedItem();
                if (seleccionada != null) {
                    controller.createOrSelectIf(
                            TabGestionarPartida.class,
                            tGP -> tGP.getPartida().equals(seleccionada),
                            () -> new TabGestionarPartida(seleccionada)
                    );
                }
            }
        });
        Label labelNuevaPartida = new Label("Crear una nueva partida");
        labelNuevaPartida.setAlignment(Pos.CENTER);
        labelNuevaPartida.getStyleClass().add("inicio-subtitle");
        labelNuevaPartida.setStyle("-fx-padding: 5 0 0 0;");
        TextField txtNuevaPartida = new TextField();
        txtNuevaPartida.setPromptText("Ingrese el nombre de la partida");
        txtNuevaPartida.setStyle("-fx-padding: 2 2 4 2;");
        txtNuevaPartida.getStyleClass().add("inicio-subtitle");
        txtNuevaPartida.setPrefWidth(400);
        Button btnEliminarPartida = new Button("Eliminar Partida");
        btnEliminarPartida.setOnAction(e -> {
            if(partidaListView.getSelectionModel().getSelectedItem() == null)
                throw new RuntimeException("No hay ninguna partida seleccionada");

            if(Dialogs.confirmarDoble("Esta seguro que desea eliminar la partida " + partidaListView.getSelectionModel().getSelectedItem().getTitulo() + "? ", partidaListView.getSelectionModel().getSelectedItem().getTitulo()))
            {   juegosService.eliminarPartida(partidaListView.getSelectionModel().getSelectedItem());
                update();
                System.out.println("Se borro la partida " + partidaListView.getSelectionModel().getSelectedItem().getTitulo() + " - " + partidaListView.getSelectionModel().getSelectedItem().getId());
            }
        });
        Button btnAgregarPartida = new Button("Agregar");
        btnAgregarPartida.setCursor(javafx.scene.Cursor.HAND);
        btnAgregarPartida.setOnAction(e -> {
            if (!txtNuevaPartida.getText().isBlank()) {
                Partida partida = new Partida(txtNuevaPartida.getText());
                juegosService.agregarPartida(juego, partida);
                update();
            }
        });
        Button btnEditarNombrePartida = new Button("Cambiar nombre");
        btnEditarNombrePartida.setCursor(javafx.scene.Cursor.HAND);
        btnEditarNombrePartida.setOnAction(e -> {
            Partida p = partidaListView.getSelectionModel().getSelectedItem();
            if(p == null){
                controller.showToast("Seleccione una partida ya creada");
                return;
            }
            Optional<String> nuevoTitulo = Dialogs.inputText("Partida " + p.getTitulo(), "Elija el nuevo nombre para la partida");
            if(nuevoTitulo.isEmpty()){
                return;
            }
            partidaService.modificarTituloPartida(p, nuevoTitulo);
            update();
            controller.findTab(TabGestionarPartida.class, (tabGestionarPartida)-> Objects.equals(tabGestionarPartida.getPartida().getId(), p.getId())).ifPresent(TabWrapper::update);
        });
        HBox agregarPartidaBox = new HBox(8, txtNuevaPartida, btnAgregarPartida, btnEliminarPartida, btnEditarNombrePartida);
        agregarPartidaBox.setStyle("-fx-alignment: center;");

        root.getChildren().addAll(lblTitulo, lblNombre, partidaListView, labelNuevaPartida, agregarPartidaBox);
        return root;
    }

}
