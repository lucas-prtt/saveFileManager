package lprtt;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

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