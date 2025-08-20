package lprtt;


import Menus.MenuPrincipal;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
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

        ConfigurableApplicationContext app =  new SpringApplicationBuilder(Cliente.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false)
                .web(WebApplicationType.NONE)
                .properties("logging.level.root=WARN")
                .run(args);
        new AppProperties(app.getEnvironment());

        System.out.println("Springboot iniciado!");
        new MenuPrincipal().abrirMenu();
        System.exit(0);
    }

}