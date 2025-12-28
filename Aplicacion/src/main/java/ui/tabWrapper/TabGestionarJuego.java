package ui.tabWrapper;

import domain.Archivos.Directorio;
import domain.Juegos.Juego;
import domain.Juegos.Partida;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import servicios.JuegosService;
import servicios.PartidaService;
import ui.MainController;

import java.nio.file.Path;
import java.util.List;

public class TabGestionarJuego extends TabWrapper {
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
    @Transactional
    public VBox getContent() {
        VBox root = new VBox(10);
        root.getStyleClass().add("root-padding");

        Label lblTitulo = new Label("Gestionar juego:");
        Label lblNombre = new Label(juego.getTitulo());
        lblNombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnEliminar = new Button("Eliminar juego");
        btnEliminar.setOnAction(e -> {
            juegosService.eliminarJuego(juego);
            close();
            controller.findTab(TabElegirJuego.class).ifPresent(TabWrapper::update);
        });


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
                    TabGestionarPartida tabGestionarPartida = new TabGestionarPartida(seleccionada);
                    controller.createTab(tabGestionarPartida);
                    tabGestionarPartida.focus();
                }
            }
        });
        Label labelNuevaPartida = new Label("Crear una nueva partida");
        TextField txtNuevaPartida = new TextField();
        txtNuevaPartida.setPromptText("Ingrese el nombre de la partida");
        txtNuevaPartida.setPrefWidth(400);

        Button btnAgregarPartida = new Button("Agregar");
        btnAgregarPartida.setCursor(javafx.scene.Cursor.HAND);
        btnAgregarPartida.setOnAction(e -> {
            if (!txtNuevaPartida.getText().isBlank()) {
                Partida partida = new Partida(txtNuevaPartida.getText());
                juegosService.agregarPartida(juego, partida);
                update();
            }
        });
        HBox agregarPartidaBox = new HBox(8, txtNuevaPartida, btnAgregarPartida);
        agregarPartidaBox.setStyle("-fx-alignment: center-left;");

        root.getChildren().addAll(lblTitulo, lblNombre, btnEliminar, partidaListView, labelNuevaPartida, agregarPartidaBox);
        return root;
    }

}
