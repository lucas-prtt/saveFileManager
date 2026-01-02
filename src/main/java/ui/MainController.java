package ui;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import servicios.DirectorySecurity;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import lombok.Getter;
import servicios.CheckpointService;
import servicios.JuegosService;
import servicios.PartidaService;
import ui.tabWrapper.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class MainController {

    private Stage stage;
    private TabPane tabPane;
    private List<TabWrapper> tabs = new ArrayList<>();
    private final JuegosService juegosService;
    private final PartidaService partidaService;
    private final CheckpointService checkpointService;
    private final DirectorySecurity directorySecurity;

    public MainController(JuegosService juegosService, PartidaService partidaService, CheckpointService checkpointService, DirectorySecurity directorySecurity) {
        this.juegosService = juegosService;
        this.partidaService = partidaService;
        this.checkpointService = checkpointService;
        this.directorySecurity = directorySecurity;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        initUI();
    }

    private void initUI() {
        tabPane = new TabPane();

        TabInicial tabInicial = new TabInicial();
        createTab(tabInicial);

        tabPane.getTabs().forEach(t -> t.setClosable(false));
        StackPane root = new StackPane(tabPane);

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
        );


        stage.setScene(scene);
        stage.setTitle("Save Files Manager");
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F11 -> stage.setFullScreen(!stage.isFullScreen());
            }
        });
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.show();
    }

    public void createAndSelectTab(TabWrapper tabWrapper) {
        if(tabWrapper instanceof TabElegirJuego || tabWrapper instanceof TabCargarJuego || tabWrapper instanceof TabSettings){
            Optional<TabWrapper> tab = (Optional<TabWrapper>) findTab(tabWrapper.getClass());
            if(tab.isPresent()){
                selectTab(tab.get());
                return;
            }
        }
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
    public <T extends TabWrapper> Optional<T> findTab(Class<T> tabClass) {
        return findTab(t -> t.getClass().equals(tabClass))
                .map(tabClass::cast);
    }
    public <T extends TabWrapper> Optional<T> findTab(Class<T> tabClass, Function<T, Boolean> condition) {
        return findTab(t -> t.getClass().equals(tabClass) && condition.apply(tabClass.cast(t)))
                .map(tabClass::cast);
    }
    public void closeTab(TabWrapper tabWrapper) {
        tabPane.getTabs().remove(tabWrapper.getTab());
        tabs.remove(tabWrapper);
    }

    public <T extends TabWrapper> T createOrSelectIf(Class<T> tabClass, Function<T, Boolean> existsCondition, Supplier<T> crearTab) {
        Optional<T> optionalT = findTab(tabClass, existsCondition);
        T tab;
        if (optionalT.isPresent()) {
            tab = optionalT.get();
        } else {
            tab = crearTab.get();
            createTab(tab);
        }
        tab.focus();
        return tab;
    }

    public void showToast(String message) {
        Platform.runLater(() -> {
            Label toast = new Label(message);
            toast.getStyleClass().add("toast");

            StackPane root = (StackPane) stage.getScene().getRoot();
            root.getChildren().add(toast);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            FadeTransition stay = new FadeTransition(Duration.seconds(5), toast);
            stay.setFromValue(1);
            stay.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), toast);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            fadeIn.setOnFinished(e -> stay.play());
            stay.setOnFinished(e -> fadeOut.play());
            fadeOut.setOnFinished(e -> root.getChildren().remove(toast));

            fadeIn.play();
        });
    }
}
