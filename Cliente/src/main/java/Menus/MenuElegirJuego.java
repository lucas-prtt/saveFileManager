package Menus;

import ApiHelper.ApiRequestManager;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerManager;

import java.util.List;

public class MenuElegirJuego extends Menu {
    private static List<String> titulosJuegos;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Ingrese 0 para volver al menu principal");
        System.out.println("Elija el juego a gestionar:");
        int i = 1;
        titulosJuegos = api.obtenerTitulosJuegos();
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
        JuegoDTO juegoElegido = api.obtenerJuego(titulosJuegos.get(opcion-1));
        System.out.println(juegoElegido.getTitulo());
        new MenuGestionarJuego(juegoElegido).abrirMenu();
        return false;
    }
}
