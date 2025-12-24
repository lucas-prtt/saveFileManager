package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import ui.tabs.TabCargarJuego;
import ui.tabs.TabElegirJuego;
import ui.tabs.TabInicial;
import ui.tabs.VistaTab;

import java.util.List;


public class MainController {

    private Stage stage;
    @Getter
    private TabPane tabPane;

    public MainController(Stage stage) {
        this.stage = stage;

        // Crear encabezado con tabs
        tabPane = new TabPane();
        Tab tab1 = new Tab("Inicio");
        Tab tab2 = new Tab("Gestionar Juego");
        Tab tab3 = new Tab("Cargar Juego");
        tab1.setContent(new TabInicial(this).getContent());
        tab2.setContent(new TabElegirJuego(this).getContent());
        tab3.setContent(new TabCargarJuego(this).getContent());
        tabPane.getTabs().addAll(tab1, tab2, tab3);
        tabPane.getTabs().forEach(t -> t.setClosable(false));

        // Escena
        stage.setScene(new Scene(tabPane, 600, 400));
        stage.setTitle("Save Files Manager");
        stage.show();
    }

    public void seleccionarTab(int index) {
        tabPane.getSelectionModel().select(index);
    }
}
