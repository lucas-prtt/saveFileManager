package Menus;

import ApiClients.JuegoClient;
import Juegos.Juego;
import ServerManagment.ServerManager;

import java.util.List;

public class MenuElegirJuego extends Menu {
    private static List<String> titulosJuegos;
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Ingrese 0 para volver al menu principal");
        System.out.println("Elija el juego a gestionar:");
        int i = 1;
        titulosJuegos = new JuegoClient().obtenerTitulosJuegos(ServerManager.getInstance().getServidorLocal());
        for(String titulo: titulosJuegos){
            System.out.println(i + ". " + titulo);
            i++;
        }
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        if (opcion == 0)
            return true;
        if(opcion > titulosJuegos.size() || opcion < 0)
            throw new Exception("Opcion invalida");
        Juego juegoElegido = new JuegoClient().obtenerJuego(ServerManager.getInstance().getServidorLocal(), titulosJuegos.get(opcion-1));
        System.out.println(juegoElegido.getTitulo());
        new MenuGestionarJuego(juegoElegido).abrirMenu();
        return false;
    }
}
