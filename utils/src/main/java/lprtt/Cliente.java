package lprtt;


import Menus.MenuPrincipal;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

public class Cliente {

    public static void main(String[] args) {
        System.out.println("Programa iniciado!");


        //System.setProperty("logging.level.root", "OFF");
        SpringApplication app = new SpringApplication(Cliente.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run();
        System.out.println("Springboot configurado!");


        new MenuPrincipal().abrirMenu();
        return;
    }

}