package Menus;

import Juegos.Juego;
import SubMenus.SubMenuAgregarPath;
import SubMenus.SubMenuElegirPartida;
import SubMenus.SubMenuEliminarPath;
import Utils.SimularTachado;

public class MenuGestionarJuego extends Menu{
    Juego juego;
    public MenuGestionarJuego(Juego juego){
        this.juego = juego;
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Elija una opcion:");
        System.out.println("1. Ver información del juego");
        System.out.println("2. Añadir path para saveFiles");
        System.out.println("3. Quitar path para saveFiles");
        System.out.println("4. Elegir Partida");
        if(!juego.getPartidas().isEmpty())
            System.out.println("5. Elegir partida actual ("+ juego.getPartidaActual().getTitulo()+")");
        else
            System.out.println(SimularTachado.tachar("5. Elegir partida actual"));
        System.out.println("6. Eliminar Partida");
        System.out.println("7. Crear Partida");
        System.out.println("8. Salir");

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
                new SubMenuElegirPartida(juego).abrirMenu();
                break;
            case 5:
                if (!juego.getPartidas().isEmpty())
                    new MenuGestionarPartida(juego.getPartidaActual(), juego).abrirMenu();
                else
                    System.out.println("Error: Opcion invalida, se debe crear una partida primero");
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                return true;
        }
        return false;
    }
}
