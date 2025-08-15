package Menus;

import Juegos.Juego;
import Juegos.Partida;

public class MenuGestionarPartida extends Menu{
    Partida partida;
    Juego juego;
    public MenuGestionarPartida(Partida partida, Juego juego){
        this.partida = partida;
        this.juego = juego;
    }

    @Override
    void mostrarTextoOpciones() {
        System.out.println("Gestionando \n > Juego: " + juego.getTitulo() + "\n > Partida: " + partida.getTitulo());
        System.out.println("Elija una opcion:");
        System.out.println("1. Guardar checkpoint");
        System.out.println("2. Cargar ultimo checkpoint");
        System.out.println("3. Ver checkpoints");
        System.out.println("4. Eliminar checkpoints");
        System.out.println("5. Salir");
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        switch (opcion){
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                return true;
        }
        return false;
    }
}
