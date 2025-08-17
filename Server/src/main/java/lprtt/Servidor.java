package lprtt;

import java.io.*;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
