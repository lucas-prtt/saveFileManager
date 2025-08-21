package SubMenus;

import ApiClients.PartidaClient;
import ApiHelper.ApiRequestManager;
import Juegos.Juego;
import Juegos.Partida;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import Menus.MenuGestionarPartida;
import ServerManagment.ServerManager;

import java.util.List;
import java.util.Scanner;

public class SubMenuElegirPartida {
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());
    public SubMenuElegirPartida(JuegoDTO juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        List<String> partidas = api.obtenerTitulosPartidas(juego.getTitulo());
        System.out.println("Elija la partida");
        int i = 1;
        for(String tituloPartida : partidas){
            System.out.println(i+". "+tituloPartida);
            i++;
        }
        int indice = new Scanner(System.in).nextInt()-1;
        PartidaDTO partida = api.obtenerPartida(juego.getTitulo(), partidas.get(indice));
        new MenuGestionarPartida(partida, juego).abrirMenu();
    }
}
