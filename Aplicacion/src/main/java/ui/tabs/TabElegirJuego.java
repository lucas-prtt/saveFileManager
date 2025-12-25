package ui.tabs;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import servicios.JuegosService;
import ui.MainController;
import javafx.scene.control.Button;

@Component
public class TabElegirJuego implements VistaTab{
    @Autowired
    private JuegosService juegosService;
    @Autowired
    private MainController controller;


    public VBox getContent() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Pantalla: Elegir Juego");
        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.seleccionarTab("Inicio"));
        Label label2 = new Label(juegosService.titulosDeTodosLosJuegos().toString());

        content.getChildren().addAll(label, label2, btnIrInicio);
        return content;
    }

}

