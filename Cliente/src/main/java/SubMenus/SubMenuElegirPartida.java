package SubMenus;

import ApiClients.PartidaClient;
import Juegos.Juego;
import Juegos.Partida;
import JuegosDtos.JuegoDTO;
import Menus.MenuGestionarPartida;
import ServerManagment.ServerManager;

import java.util.List;
import java.util.Scanner;

public class SubMenuElegirPartida {
    JuegoDTO juego;
    public SubMenuElegirPartida(JuegoDTO juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        List<String> partidas = new PartidaClient().obtenerTitulosPartidas(ServerManager.getInstance().getServidorLocal(), juego.getTitulo());
        System.out.println("Elija la partida");
        int i = 1;
        for(String tituloPartida : partidas){
            System.out.println(i+". "+tituloPartida);
            i++;
        }
        int indice = new Scanner(System.in).nextInt()-1;
        Partida partida = new PartidaClient().obtenerPartida(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), partidas.get(indice));
        new MenuGestionarPartida(partida, juego).abrirMenu();

    }
}
