package ui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lprtt.Main;
import org.springframework.stereotype.Component;
import ui.tabs.TabInicial;
import ui.tabs.VistaTab;

import java.util.Objects;
import java.util.Optional;

@Component
public class MainController {

    private Stage stage;

    @Getter
    private TabPane tabPane;

    public void setStage(Stage stage) {
        this.stage = stage;
        initUI();
    }

    private void initUI() {
        tabPane = new TabPane();

        TabInicial tabInicial = Main.getContext().getBean(TabInicial.class);
        addOrUpdateTab("Inicio", tabInicial.getContent());

        tabPane.getTabs().forEach(t -> t.setClosable(false));
        Scene scene = new Scene(tabPane, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Save Files Manager");
        stage.show();
    }

    public void seleccionarTab(String tab) {
        tabPane.getSelectionModel().select(findTab(tab).orElseThrow(() -> new RuntimeException("No se encontro el tab")));
    }

    public void seleccionarOCrearTab(String tabTitulo, Class<? extends VistaTab> clazz) {
        VistaTab tabBean = Main.getContext().getBean(clazz);
        VBox content = tabBean.getContent();
        Tab tab = addOrUpdateTab(tabTitulo, content);
        tabPane.getSelectionModel().select(tab);
    }

    public Tab addOrUpdateTab(String titulo, VBox content) {
        Optional<Tab> existente = findTab(titulo);
        if (existente.isPresent()) {
            existente.get().setContent(content);
            return existente.get();
        }
        Tab tab = new Tab(titulo);
        tab.setContent(content);
        tabPane.getTabs().add(tab);
        return tab;
    }

    public Optional<Tab> findTab(String titulo) {
        return tabPane.getTabs().stream().filter(tab -> Objects.equals(tab.getText(), titulo)).findFirst();
    }
}
