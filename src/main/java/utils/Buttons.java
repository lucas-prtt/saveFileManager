package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Buttons {
    public static HBox checkBox(String checkText, Supplier<String> labelText, Supplier<Boolean> getcheckValue, Consumer<Boolean> setCheckValue){
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox content = new HBox(5);
        Label label = new Label(labelText.get());

        CheckBox check = new CheckBox(checkText);
        check.setSelected(getcheckValue.get());
        check.selectedProperty().addListener((obs, oldVal, newVal) -> {
            setCheckValue.accept(newVal);
            label.setText(labelText.get());
        });
        content.setPadding(new Insets(5));
        content.getChildren().addAll(check, spacer, label);
        return content;
    }

    public static HBox fileSizeField(
            String labelText,
            Supplier<Long> getValueInBytes,
            Consumer<Long> setValueInBytes
    ) {
        Region spacer = new Region();
        spacer.setPrefWidth(5);

        Label label = new Label(labelText);
        label.getStyleClass().add("check-box");

        TextField valueField = new TextField();
        valueField.setPrefWidth(80);

        ComboBox<FileSizeUnit> unitBox = new ComboBox<>();
        unitBox.getItems().addAll(FileSizeUnit.values());
        unitBox.setValue(FileSizeUnit.MB);

        long bytes = getValueInBytes.get();
        FileSizeUnit bestUnit = FileSizeUnit.bestFit(bytes);
        unitBox.setValue(bestUnit);
        valueField.setText(String.valueOf(bestUnit.fromBytes(bytes)));

        Runnable updateConsumer = () -> {
            try {
                long value = Long.parseLong(valueField.getText());
                long resultBytes = unitBox.getValue().toBytes(value);
                setValueInBytes.accept(resultBytes);
            } catch (NumberFormatException ignored) {}
        };

        valueField.textProperty().addListener((obs, o, n) -> updateConsumer.run());
        unitBox.valueProperty().addListener((obs, o, n) -> updateConsumer.run());

        HBox content = new HBox(5);
        content.setPadding(new Insets(5));
        content.setAlignment(Pos.CENTER_LEFT);
        content.getChildren().addAll(label, spacer, valueField, unitBox);

        return content;
    }
}
