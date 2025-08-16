package lprtt;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Properties;
import org.h2.tools.Server;

public class BaseDeDatos {
    public static void main(String[] args) {
        try {
            Path jarDir = Paths.get(BaseDeDatos.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            Path configPath = jarDir.resolve("config/h2-config.properties");

            // Crear archivo de configuración si no existe
            if (Files.notExists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Properties defaultProps = new Properties();
                defaultProps.setProperty("h2.port", "9092");
                defaultProps.setProperty("h2.user", "admin");
                defaultProps.setProperty("h2.password", "admin");
                defaultProps.setProperty("h2.db.path", "./data/database");
                try (OutputStream out = Files.newOutputStream(configPath)) {
                    defaultProps.store(out, "Configuración por defecto de H2");
                }
                System.out.println("Archivo de configuración creado por defecto en: " + configPath);
            }

            // Cargar configuración
            Properties props = new Properties();
            try (InputStream in = Files.newInputStream(configPath)) {
                props.load(in);
                System.out.println("Configuración cargada desde: " + configPath);
            }

            String port = props.getProperty("h2.port", "9092");
            String user = props.getProperty("h2.user", "admin");
            String password = props.getProperty("h2.password", "admin");
            String dbPath = props.getProperty("h2.db.path", "./data/database");

            Server server = Server.createTcpServer(
                    "-tcpPort", port,
                    "-tcpAllowOthers",
                    "-ifNotExists"
            ).start();

            System.out.println("Servidor H2 iniciado en puerto: " + port);
            System.out.println("Conectar con: jdbc:h2:tcp://localhost:" + port + "/" + dbPath);
            System.out.println("Usuario: " + user + " | Contraseña: " + password);
            System.out.println("Presiona Enter para detener el servidor...");
            System.in.read();
            server.stop();
            System.out.println("Servidor detenido.");

        } catch (IOException | URISyntaxException | java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
