package ui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lprtt.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ui.tabs.TabFactory;
import ui.tabs.TabInicial;
import ui.tabs.VistaTab;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class MainController {

    private Stage stage;

    @Autowired
    TabFactory tabFactory;
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


    public <T extends VistaTab> T seleccionarOCrearTab(Class<T> tabClazz, Consumer<T> accion) {
        T tabBean = Main.getContext().getBean(tabClazz);
        if(accion!=null)
            accion.accept(tabBean);
        seleccionarOCrearTab(tabBean);
        return tabBean;
    }
    public <T extends VistaTab> T seleccionarOCrearTab(Class<T> tabClazz) {
        return seleccionarOCrearTab(tabClazz, null);
    }
    public VistaTab seleccionarOCrearTab(VistaTab tabBean) {
        VBox content = tabBean.getContent();
        Tab tab = addOrUpdateTab(tabBean.getName(), content);
        tabPane.getSelectionModel().select(tab);
        return tabBean;
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
        if(titulo == null){return Optional.empty();}
        return tabPane.getTabs().stream().filter(tab -> Objects.equals(tab.getText(), titulo)).findFirst();
    }
    public void cerrarTab(String titulo) {
        Optional<Tab> tab = tabPane.getTabs().stream().filter(t -> Objects.equals(t.getText(), titulo)).findFirst();
        tab.ifPresent(value -> tabPane.getTabs().remove(value));
    }
    public void refresh(Class<? extends VistaTab> tabClazz){
        VistaTab vistaTab = Main.getContext().getBean(tabClazz);
        findTab(vistaTab.getName()).ifPresent(tab1 -> tab1.setContent(vistaTab.getContent()));

    }
}
