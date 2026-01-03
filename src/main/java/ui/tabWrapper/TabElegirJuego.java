package ui.tabWrapper;

import domain.Juegos.Juego;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import servicios.JuegosService;
import ui.MainController;
import utils.Dialogs;

import java.util.List;

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
            if(listJuegos.getSelectionModel().getSelectedItem() == null)
                throw new RuntimeException("No hay ningun juego seleccionado");
            if(Dialogs.confirmarDoble("Esta seguro que desea eliminar " + listJuegos.getSelectionModel().getSelectedItem().getTitulo()+ "? ", listJuegos.getSelectionModel().getSelectedItem().getTitulo())) {
                juegosService.eliminarJuego(listJuegos.getSelectionModel().getSelectedItem());
                update();
                System.out.println("Se borro el juego" + listJuegos.getSelectionModel().getSelectedItem().getTitulo() + " - " + listJuegos.getSelectionModel().getSelectedItem().getId());
            }//TODO: Cerrar tabs de partidas y checkpoints del juego
        });
        HBox botones = new HBox(10);
        botones.setPadding(new Insets(10));
        botones.setAlignment(Pos.CENTER);

        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.findTab(TabInicial.class).ifPresent(TabWrapper::focus));
        botones.getChildren().addAll(  btnIrInicio, btnEliminar);
        content.getChildren().addAll(label, listJuegos, botones);
        return content;
    }

}

