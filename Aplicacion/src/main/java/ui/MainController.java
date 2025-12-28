package ui;

import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import lombok.Getter;
import lprtt.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import servicios.CheckpointService;
import servicios.JuegosService;
import servicios.PartidaService;
import ui.tabWrapper.TabInicial;
import ui.tabWrapper.TabWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
@Getter
@Component
public class MainController {

    private Stage stage;
    private TabPane tabPane;
    private List<TabWrapper> tabs = new ArrayList<>();
    @Autowired
    JuegosService juegosService;
    @Autowired
    PartidaService partidaService;
    @Autowired
    CheckpointService checkpointService;

    public void setStage(Stage stage) {
        this.stage = stage;
        initUI();
    }

    private void initUI() {
        tabPane = new TabPane();

        TabInicial tabInicial = new TabInicial();
        createTab(tabInicial);

        tabPane.getTabs().forEach(t -> t.setClosable(false));
        Scene scene = new Scene(tabPane, 600, 400);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.setTitle("Save Files Manager");
        stage.show();
    }

    public void createAndSelectTab(TabWrapper tabWrapper) {
        createTab(tabWrapper);
        selectTab(tabWrapper);
    }

    public void selectTab(TabWrapper tabWrapper) {
        tabPane.getSelectionModel().select(tabWrapper.getTab());
    }

    public void createTab(TabWrapper tabWrapper){
        tabWrapper.init(this);
        tabWrapper.update();
        tabs.add(tabWrapper);
        tabPane.getTabs().add(tabWrapper.getTab());
    }

    public Optional<TabWrapper> findTab(Function<TabWrapper, Boolean> condition) {
        return tabs.stream().filter(condition::apply).findFirst();
    }
    /**
     *  @return El primer {@link TabWrapper} de la clase
     * */
    public Optional<TabWrapper> findTab(Class<? extends TabWrapper> tabClass) {
        return findTab(tabClass::isInstance);
    }
    public Optional<TabWrapper> findTab(Class<? extends TabWrapper> tabClass, Function<TabWrapper, Boolean> condition) {
        return findTab(t->tabClass.isInstance(t) && condition.apply(t));
    }
    public void closeTab(TabWrapper tabWrapper) {
        tabPane.getTabs().remove(tabWrapper.getTab());
    }
}
