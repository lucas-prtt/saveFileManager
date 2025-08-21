package SubMenus;

import ApiHelper.ApiHelper;
import ApiHelper.ApiRequestManager;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerManager;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class SubMenuEliminarPartida {
    JuegoDTO juego;
    List<String> titulosPartidas;
    String partidaActual;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());

    public SubMenuEliminarPartida(JuegoDTO juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        titulosPartidas = api.obtenerTitulosPartidas(juego.getTitulo());
        partidaActual = api.obtenerPartidaActual(juego.getTitulo());
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
        ApiHelper.eliminarPartida(api, juego.getTitulo(), titulosPartidas.get(indice));
        if(Objects.equals(partidaActual, titulosPartidas.getFirst())){
            ApiHelper.cambiarPartidaActual(api, juego.getTitulo(), null);
        }
        return;
    }
}
