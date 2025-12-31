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
        VBox root = new VBox();
        root.getStyleClass().add("inicio-root");

        VBox card = new VBox(20);
        card.getStyleClass().add("inicio-card");

        Label title = new Label("Save Files Manager");
        title.getStyleClass().add("inicio-title");

        Label subtitle = new Label("Menú principal");
        subtitle.getStyleClass().add("inicio-subtitle");

        Button btnCargar = new Button("📂  Cargar Juego");
        btnCargar.getStyleClass().add("menu-button");
        btnCargar.setOnAction(e -> controller.createAndSelectTab(new TabCargarJuego()));

        Button btnGestionar = new Button("🎮  Elegir Juego");
        btnGestionar.getStyleClass().add("menu-button");
        btnGestionar.setOnAction(e -> controller.createAndSelectTab(new TabElegirJuego()));

        Button btnSettings = new Button("⚙ Opciones");
        btnSettings.getStyleClass().add("menu-button");
        btnSettings.setOnAction(e -> controller.createAndSelectTab(new TabSettings()));

        card.getChildren().addAll(
                title,
                subtitle,
                btnCargar,
                btnGestionar,
                btnSettings
        );

        root.getChildren().add(card);
        return root;
    }
    @Override
    protected boolean isTabCloseable(){
        return false;
    }
}
