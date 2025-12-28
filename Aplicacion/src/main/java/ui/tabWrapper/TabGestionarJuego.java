package ui.tabWrapper;

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

public class TabGestionarJuego extends TabWrapper {
    private Juego juego;
    private JuegosService juegosService;

    public String getName(){
        return juego.getTitulo();
    }

    public TabGestionarJuego(Juego juego){
        super();
        this.juego = juego;
    }

    @Override
    public void init(MainController mainController) {
        super.init(mainController);
        juegosService = mainController.getJuegosService();
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
            juegosService.eliminarJuego(juego);
            close();
            controller.findTab(TabElegirJuego.class).ifPresent(TabWrapper::update);
        });

        root.getChildren().addAll(lblTitulo, lblNombre, btnEliminar);
        return root;
    }

}
