package SubMenus;

import ApiHelper.ApiRequestManager;
import ApiHelper.ApiRequestHelper;
import JuegosDtos.JuegoDTO;
import JuegosDtos.PartidaDTO;
import ServerManagment.ServerManager;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuCrearPartida {
    JuegoDTO juego;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());

    public SubMenuCrearPartida(JuegoDTO juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        System.out.println("Ingrese el nombre de la partida");
        String name = new Scanner(System.in).nextLine();
        if(Objects.equals(name, "")){
            System.out.println("Creacion cancelada. Se requiere ingresar un nombre");
            return;
        }
        ApiRequestHelper.crearPartida(api, juego.getTitulo(), name);
        System.out.println("Partida creada");
        if(api.obtenerTitulosPartidas(juego.getTitulo()).isEmpty()){
            ApiRequestHelper.cambiarPartidaActual(api, juego.getTitulo(), partida.getTituloPartida());
            System.out.println("Partida asignada como partida actual, por ser la unica disponible");
        }
        return;
    }
}
