package ui.tabs;

import domain.Archivos.Directorio;
import domain.Juegos.Juego;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import servicios.JuegosService;
import ui.MainController;

import java.util.List;

@Component
public class TabElegirJuego implements VistaTab{
    @Autowired
    private JuegosService juegosService;
    @Autowired
    private MainController controller;
    @Autowired
    private TabFactory tabFactory;

    public String getName(){
        return "Elegir Juego";
    }

    public VBox getContent() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label label = new Label("Elegir Juego");

        List<String> juegos = juegosService.titulosDeTodosLosJuegos();
        ObservableList<String> juegosObservable = FXCollections.observableArrayList(juegosService.titulosDeTodosLosJuegos());
        ListView<String> listJuegos = new ListView<>();
        listJuegos.setItems(juegosObservable);


        listJuegos.setCellFactory(lv -> new ListCell<>() {

            private final Label labelJuego = new Label();
            private final HBox content = new HBox(10);
            {
                HBox.setHgrow(content, Priority.ALWAYS);
                content.getChildren().addAll(labelJuego);
                content.getStyleClass().add("path-row");
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    labelJuego.setText(item);
                    setGraphic(content);
                }
            }
        });
        listJuegos.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String seleccionado = listJuegos.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    controller.seleccionarOCrearTab(TabGestionarJuego.class,
                            (tab -> tab.setJuego(juegosService.obtenerJuegoPorTitulo(seleccionado)))
                    );
                }
            }
        });
        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.seleccionarTab("Inicio"));

        content.getChildren().addAll(label, listJuegos, btnIrInicio);
        return content;
    }

}

