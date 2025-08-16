package lprtt;


import Menus.MenuPrincipal;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

public class Main {

    public static void main(String[] args) {
        System.out.println("Programa iniciado!");
        //System.setProperty("logging.level.root", "OFF");
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);
        app.run();
        new MenuPrincipal().abrirMenu();
    }

}