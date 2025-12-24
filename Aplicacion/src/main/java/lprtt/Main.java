package lprtt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controladores", "Servicios", "dto/JuegosConverter"})
@EnableJpaRepositories(basePackages = "Repositorios")
@EntityScan(basePackages = {"domain/Juegos", "domain/Archivos"})


public class Main {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        System.out.println("Iniciando cliente...");

        context = new SpringApplicationBuilder(Main.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false)
                .web(WebApplicationType.NONE)
                .properties("logging.level.root=WARN")
                .run(args);

        System.out.println("Springboot iniciado!");

        JavaFxApp.launch(JavaFxApp.class, args);
    }

    public static class JavaFxApp extends Application {

        @Override
        public void start(Stage stage) {
            Label label = new Label("Hola! Este es un test de interfaz!");

            Button button = new Button("Click aquí");
            button.setOnAction(e -> label.setText("Click!"));

            VBox layout = new VBox(10, label, button);
            layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

            Scene scene = new Scene(layout, 400, 200);
            stage.setTitle("SaveFileManager");
            stage.setScene(scene);
            stage.show();
        }

        @Override
        public void stop() {
            context.close();
        }
    }
}