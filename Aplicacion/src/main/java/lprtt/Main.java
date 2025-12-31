package lprtt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ui.MainController;

@SpringBootApplication
@ComponentScan(basePackages = {"controladores", "servicios", "dto/JuegosConverter", "ui", "domain/Archivos"})
@EnableJpaRepositories(basePackages = "repositorios")
@EntityScan(basePackages = {"domain/Juegos", "domain/Archivos"})


public class Main {
    @Getter
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
            MainController controller = context.getBean(MainController.class);
            controller.setStage(stage);
        }

        @Override
        public void stop() {
            context.close();
        }
    }
}