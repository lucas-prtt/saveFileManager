package ui.tabs;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ui.MainController;

@Component
public class TabCargarJuego implements VistaTab{

    @Autowired
    private MainController controller;

    public VBox getContent() {

        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Pantalla: Cargar Juego");
        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.seleccionarTab("Inicio"));

        content.getChildren().addAll(label, btnIrInicio);
        return content;
    }

}

