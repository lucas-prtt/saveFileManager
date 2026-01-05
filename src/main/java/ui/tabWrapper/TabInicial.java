package ui.tabWrapper;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utils.I18nManager;

public class TabInicial extends TabWrapper {

    public String getName(){
        return I18nManager.get("MenuInicio");
    }
    @Override
    public VBox getContent() {
        VBox root = new VBox();
        root.getStyleClass().add("inicio-root");

        VBox card = new VBox(20);
        card.getStyleClass().add("inicio-card");

        Label title = new Label("Save Files Manager");
        title.getStyleClass().add("inicio-title");

        Label subtitle = new Label(I18nManager.get("MenuInicio"));
        subtitle.getStyleClass().add("inicio-subtitle");

        Button btnCargar = new Button(I18nManager.get("MenuCargarJuego"));
        btnCargar.getStyleClass().add("menu-button");
        btnCargar.setOnAction(e -> controller.createAndSelectTab(new TabCargarJuego()));

        Button btnGestionar = new Button(I18nManager.get("MenuElegirJuego"));
        btnGestionar.getStyleClass().add("menu-button");
        btnGestionar.setOnAction(e -> controller.createAndSelectTab(new TabElegirJuego()));

        Button btnSettings = new Button(I18nManager.get("MenuOpciones"));
        btnSettings.getStyleClass().add("menu-button");
        btnSettings.setOnAction(e -> controller.createAndSelectTab(new TabSettings()));

        card.getChildren().addAll(
                selectorDeLenguaje(),
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

    private HBox selectorDeLenguaje() {
        HBox header = new HBox();
        header.setAlignment(Pos.TOP_RIGHT);
        header.setSpacing(10);

        ComboBox<String> languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll(I18nManager.getLanguageKeys());
        languageSelector.setValue(I18nManager.getLang());

        languageSelector.setCellFactory(cb -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String langCode, boolean empty) {
                super.updateItem(langCode, empty);
                if (empty || langCode == null) {
                    setText(null);
                } else {
                    String fullName = I18nManager.getLanguageFullWord(langCode);
                    setText(fullName);
                }
            }
        });
        languageSelector.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String langCode, boolean empty) {
                super.updateItem(langCode, empty);
                if (empty || langCode == null) {
                    setText(null);
                } else {
                    String fullName = I18nManager.getLanguageFullWord(langCode);
                    setText(fullName);
                }
            }
        });

        languageSelector.setPrefWidth(140);
        languageSelector.setMinWidth(140);
        languageSelector.setOnAction(e -> {
            String selectedLang = languageSelector.getValue();
            I18nManager.setLang(selectedLang);
            this.update();
        });

        languageSelector.getStyleClass().add("language-combobox");

        header.getChildren().add(languageSelector);
        return header;
    }

}
