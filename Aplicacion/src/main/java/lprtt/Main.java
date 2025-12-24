package lprtt;

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
    public static void main(String[] args) {
        System.out.println("Iniciando cliente...");

        ConfigurableApplicationContext app =  new SpringApplicationBuilder(Main.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false)
                .web(WebApplicationType.NONE)
                .properties("logging.level.root=WARN")
                .run(args);

        System.out.println("Springboot iniciado!");
    }
}