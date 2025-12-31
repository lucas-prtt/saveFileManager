package ui.tabWrapper;

import domain.Juegos.Juego;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import servicios.JuegosService;
import ui.MainController;

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
        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.findTab(TabInicial.class).ifPresent(TabWrapper::focus));

        content.getChildren().addAll(label, listJuegos, btnIrInicio);
        return content;
    }

}

