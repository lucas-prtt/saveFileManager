package lprtt;

import java.io.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"Controladores"})
@EnableJpaRepositories(basePackages = "Repositorios")
@EntityScan(basePackages = {"Juegos"})
public class Servidor {
    public static void main(String[] args) {
        SpringApplication.run(Servidor.class, args);
        try{
            System.out.println("Presiona Enter para detener el servidor...");
            System.in.read();
            System.out.println("Servidor detenido.");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
