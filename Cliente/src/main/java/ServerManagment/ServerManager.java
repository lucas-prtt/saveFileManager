package ServerManagment;

import Exceptions.ServerNotFoundException;
import lprtt.AppProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerManager {
    private Integer puertoDefault;
    private final List<ServerConnection> servidoresRegistrados = new ArrayList<>();
    private static ServerManager instance;
    //Singleton
    private ServerManager(){
        puertoDefault = AppProperties.getInstance().defaultPort();
        registrarLocalHost();
    }
    public static ServerManager getInstance(){
        if(instance == null ){
            instance = new ServerManager();
        }
        return instance;
    }


    public void registrarLocalHost(){
        registrarServidor("localhost", "Servidor local");
    }
    public void registrarLocalHost(Integer puerto){
        registrarServidor("localhost", "Servidor local", puerto);
    }
    public void registrarServidor(String servidor, String nombre, Integer puerto) {
        servidoresRegistrados.add(new ServerConnection(servidor, puerto, nombre));
    }
    public void registrarServidor(String servidor, String nombre) {
        servidoresRegistrados.add(new ServerConnection(servidor, puertoDefault, nombre));
    }
    public List<ServerConnection> getServerConnections(){
        return servidoresRegistrados;
    }
    public ServerConnection getServidorLocal(){
        return getServidorByIp("localhost");
    }
    public ServerConnection getServidorByIp(String ip){
        final Optional<ServerConnection> serverConnection = servidoresRegistrados.stream().filter(serv -> serv.ipEquals(ip)).findFirst();
        if(serverConnection.isEmpty()) {
            throw new ServerNotFoundException("No se encontro el servidor");
        }
        return serverConnection.get();
    }
}
