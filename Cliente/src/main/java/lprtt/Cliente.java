package lprtt;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;
@ComponentScan(basePackages = {"ApiClients", "ServerManagment"})
@EnableJpaRepositories(basePackages = "Repositorios")
@EntityScan(basePackages = {"Juegos", "Archivos"})
@SpringBootApplication
public class Cliente {

    public static void main(String[] args) {
        System.out.println("Iniciando cliente...");

        //System.setProperty("logging.level.root", "OFF");
        SpringApplication app = new SpringApplication(Cliente.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.setDefaultProperties(Collections.singletonMap("logging.level.root", "WARN"));
        System.out.println("Springboot configurado!");
        app.run();
        System.out.println("Springboot iniciado!");

        return;
    }

}