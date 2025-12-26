package ui.tabs;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ui.MainController;

@Component
public class TabInicial implements VistaTab {

    private final MainController controller;
    @Autowired
    public TabInicial(MainController controller) {
        this.controller = controller;
    }

    public String getName(){
        return "Inicio";
    }

    @Override
    public VBox getContent() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Pantalla: Inicio");

        Button btnCargar = new Button("Ir a Cargar Juego");
        btnCargar.setOnAction(e -> controller.seleccionarOCrearTab(TabCargarJuego.class));

        Button btnGestionar = new Button("Ir a Elegir Juego");
        btnGestionar.setOnAction(e -> controller.seleccionarOCrearTab(TabElegirJuego.class));

        content.getChildren().addAll(label, btnCargar, btnGestionar);
        return content;
    }
}
