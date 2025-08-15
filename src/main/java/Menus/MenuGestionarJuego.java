package Menus;

import Juegos.Juego;

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
        System.out.println("5. Elegir Partida actual ("+ juego.getPartidaActual().getTitulo()+")");
        System.out.println("6. Eliminar Partida");
        System.out.println("7. Salir");

    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:
                System.out.println("Juego seleccionado: " + juego.getTitulo());
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                return true;
        }
        return false;
    }
}
