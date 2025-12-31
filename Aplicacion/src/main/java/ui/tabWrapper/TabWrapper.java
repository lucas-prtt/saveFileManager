package ui.tabWrapper;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;
import ui.MainController;

@Getter
public abstract class TabWrapper {
    protected Tab tab;
    protected MainController controller;
    public abstract VBox getContent();
    public abstract String getName();
    public void update(){
        tab.setContent(getContent());
    }
    public void close(){
        controller.closeTab(this);
    }
    public void focus(){
        controller.selectTab(this);
    }
    public TabWrapper(){
        this.tab = new Tab();
    }
    /**
     * Especie de patron visitor que permite setear el controller y otras cosas que dependan del mismo.
     * <p>
     * También setea el nombre del tab, por lo que cualquier atributo que se use para ello debe estar ya seteado, ya sea desde el constructor o llamando a un setter antes del init
     * */
    public void init(MainController mainController){
        //  Visitor?
        tab.setText(getName());
        tab.setClosable(false);
        tab.setOnCloseRequest(event -> {
            controller.getTabs().remove(this);
        });

        HBox tabBox = new HBox(new Label(tab.getText()));
        if(isTabCloseable()){
            Label closeLabel = new Label("✖");
            closeLabel.getStyleClass().add("custom-tab-close");
            closeLabel.setOnMouseClicked(e -> controller.closeTab(this));
            tabBox.getChildren().add(closeLabel);

        }
        tabBox.setSpacing(8);
        tabBox.setAlignment(Pos.CENTER);

        tabBox.setSpacing(8);
        tab.setGraphic(tabBox);
        tab.setText("");

        controller = mainController;
        // Se puede Overridear para setear otros services / inyectar depenencias desde el controller
    }

    protected boolean isTabCloseable(){
        return true;
    }

}
