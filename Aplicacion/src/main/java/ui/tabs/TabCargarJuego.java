package ui.tabs;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ui.MainController;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.MainController;

public class TabCargarJuego implements VistaTab{

    private MainController controller;
    @Getter
    private VBox content;

    public TabCargarJuego(MainController controller) {
        this.controller = controller;

        content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Pantalla: Cargar Juego");
        Button btnIrInicio = new Button("Volver al Inicio");
        btnIrInicio.setOnAction(e -> controller.seleccionarTab(0)); // Tab Inicio

        content.getChildren().addAll(label, btnIrInicio);
    }

}

