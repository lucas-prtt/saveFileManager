package lprtt;
import org.h2.tools.Server;

import java.io.IOException;
import java.sql.SQLException;

public class BaseDeDatos {
    public static void main(String[] args){
        Server server = null;
        try {
            server = Server.createTcpServer("-tcpAllowOthers").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Servidor H2 iniciado y escuchando en: " + server.getURL());
        System.out.println("Presiona ENTER para detener el servidor...");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.stop();
        System.out.println("Servidor detenido.");
    }
}
