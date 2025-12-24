package ui.tabs;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ui.MainController;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.MainController;

public class TabInicial implements VistaTab{

    private MainController controller;
    @Getter
    private VBox content;

    public TabInicial(MainController controller) {
        this.controller = controller;

        content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Pantalla: Inicio");
        Button btnCargar = new Button("Ir a Cargar Juego");
        btnCargar.setOnAction(e -> controller.seleccionarTab(2)); // Tab Cargar Juego

        Button btnGestionar = new Button("Ir a Gestionar Juego");
        btnGestionar.setOnAction(e -> controller.seleccionarTab(1)); // Tab Gestionar Juego

        content.getChildren().addAll(label, btnCargar, btnGestionar);
    }
}
