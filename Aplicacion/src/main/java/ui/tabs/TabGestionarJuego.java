package ui.tabs;

import domain.Juegos.Juego;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import servicios.JuegosService;
import ui.MainController;

@Component
@Scope("prototype")
public class TabGestionarJuego implements VistaTab {
    @Setter
    private Juego juego;

    @Autowired
    private JuegosService juegosService;

    @Autowired
    private MainController controller;

    public String getName(){
        return juego.getTitulo();
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox(10);
        root.getStyleClass().add("root-padding");

        Label lblTitulo = new Label("Gestionar juego:");
        Label lblNombre = new Label(juego.getTitulo());
        lblNombre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnEliminar = new Button("Eliminar juego");
        btnEliminar.setOnAction(e -> {
            juegosService.eliminarJuego(juego.getTitulo());
            controller.cerrarTab(juego.getTitulo());
            controller.refresh(TabElegirJuego.class);
        });

        root.getChildren().addAll(lblTitulo, lblNombre, btnEliminar);
        return root;
    }

    public String getTabTitle() {
        return juego.getTitulo();
    }
}
