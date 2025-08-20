package SubMenus;

import ApiClients.JuegoClient;
import ApiClients.PartidaClient;
import Juegos.Juego;
import ServerManagment.ServerManager;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SubMenuEliminarPartida {
    Juego juego;
    List<String> titulosPartidas;
    String partidaActual;
    public SubMenuEliminarPartida(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        titulosPartidas = new PartidaClient().obtenerTitulosPartidas(ServerManager.getInstance().getServidorLocal(), juego.getTitulo());
        partidaActual = new PartidaClient().obtenerPartidaActual(ServerManager.getInstance().getServidorLocal(), juego.getTitulo());
        System.out.println("Elija la partida para eliminar (Ingrese 0 para cancelar)");
        int i = 1;
        for(String tituloPartida : titulosPartidas){
            System.out.println(i+". "+tituloPartida);
            i++;
        }
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1)
            return;
        System.out.println("Eliminando la partida <"+titulosPartidas.get(indice)+">");
        new PartidaClient().eliminarPartida(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), titulosPartidas.get(indice));
        if(Objects.equals(partidaActual, titulosPartidas.getFirst())){
            juego.setPartidaActual(null);
            System.out.println("Removida de partida actual");
        }
        return;
    }
}
