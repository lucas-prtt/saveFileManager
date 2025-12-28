package ui.tabWrapper;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TabInicial extends TabWrapper {

    public String getName(){
        return "Inicio";
    }

    @Override
    public VBox getContent() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label label = new Label("Pantalla: Inicio");

        Button btnCargar = new Button("Ir a Cargar Juego");
        btnCargar.setOnAction(e -> controller.createAndSelectTab(new TabCargarJuego()));

        Button btnGestionar = new Button("Ir a Elegir Juego");
        btnGestionar.setOnAction(e -> controller.createAndSelectTab(new TabElegirJuego()));

        content.getChildren().addAll(label, btnCargar, btnGestionar);
        return content;
    }
}
