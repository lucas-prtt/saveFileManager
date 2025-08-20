package SubMenus;

import ApiClients.JuegoClient;
import ApiClients.PartidaClient;
import Juegos.Juego;
import Juegos.Partida;
import ServerManagment.ServerManager;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuCrearPartida {
    Juego juego;
    public SubMenuCrearPartida(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        System.out.println("Ingrese el nombre de la partida");
        String name = new Scanner(System.in).nextLine();
        if(Objects.equals(name, "")){
            System.out.println("Creacion cancelada. Se requiere ingresar un nombre");
            return;
        }
        Partida partida = new Partida(name, juego);
        juego.agregarPartida(partida);
        new PartidaClient().postearPartida(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), partida);
        System.out.println("Partida creada");
        if(juego.getPartidas().size() == 1){
            juego.setPartidaActual(juego.getPartidas().getFirst());
            System.out.println("Partida asignada como partida actual, por ser la unica disponible");
        }
        new JuegoClient().patchearJuego(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego.toDTO());
        return;
    }
}
