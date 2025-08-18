package lprtt;

import java.io.*;
import java.util.Collections;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"Controladores"})
@EnableJpaRepositories(basePackages = "Repositorios")
@EntityScan(basePackages = {"Juegos", "Archivos"})
public class Servidor {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Servidor.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.setDefaultProperties(Collections.singletonMap("logging.level.root", "WARN"));
        app.run();
        try{
            System.out.println("Servidor iniciado");
            System.out.println("Presiona Enter para cerrar el servidor...");
            System.in.read();
            System.out.println("Cerrando servidor...");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
