package Menus;

import ApiHelper.ApiRequestManager;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerManager;
import SubMenus.*;
import Utils.SimularTachado;

import java.util.List;

public class MenuGestionarJuego extends Menu{
    JuegoDTO juego;
    String partidaActual;
    List<String> titulosPartidas;
    private final ApiRequestManager api = new ApiRequestManager(ServerManager.getInstance().getServidorLocal());
    public MenuGestionarJuego(JuegoDTO juego){
        this.juego = juego;
    }

    @Override
    void mostrarTextoOpciones() {
        partidaActual = api.obtenerPartidaActual(juego.getTitulo());
        titulosPartidas = api.obtenerTitulosPartidas(juego.getTitulo());
        System.out.println("Elija una opcion:");
        System.out.println("1. Ver información del juego");
        System.out.println("2. Añadir path para saveFiles");
        System.out.println("3. Quitar path para saveFiles");
        if(titulosPartidas == null || titulosPartidas.isEmpty())
            System.out.println(SimularTachado.tachar("4. Elegir partida"));
        else
            System.out.println("4. Elegir Partida");
        if(partidaActual != null)
            System.out.println("5. Elegir partida actual ("+ partidaActual +")");
        else
            System.out.println(SimularTachado.tachar("5. Elegir partida actual"));
        System.out.println("6. Eliminar Partida");
        System.out.println("7. Crear Partida");
        System.out.println("8. Vaciar archivos de guardado");
        System.out.println("9. Salir");

    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                System.out.println("Juego seleccionado: " + juego.getTitulo());
                break;
            case 2:
                new SubMenuAgregarPath(juego).abrirMenu();
                break;
            case 3:
                new SubMenuEliminarPath(juego).abrirMenu();
                break;
            case 4:
                if (titulosPartidas == null || titulosPartidas.isEmpty())
                    System.out.println("Error: Opcion invalida, se debe crear una partida primero");
                else
                    new SubMenuElegirPartida(juego).abrirMenu();
                break;
            case 5:
                if (partidaActual != null)
                    new MenuGestionarPartida(api.obtenerPartida(juego.getTitulo(), partidaActual), juego).abrirMenu();
                else
                    System.out.println("Error: Opcion invalida, se debe asignar una partida actual primero. Puede hacer esto cargandola desde la opcion superior.");
                break;
            case 6:
                new SubMenuEliminarPartida(juego).abrirMenu();
                break;
            case 7:
                new SubMenuCrearPartida(juego).abrirMenu();
                break;
            case 8:
                juego.setTituloPartidaActual(null);
                //juego.vaciarArchivosDeGuardado();
                api.patchearJuego(juego.getTitulo(), juego);
                break;
            case 9:
                return true;
        }
        return false;
    }
}
