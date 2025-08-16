package Menus;

import Controllers.JuegosController;
import Juegos.Juego;

public class MenuElegirJuego extends Menu {
    @Override
    void mostrarTextoOpciones() {
        System.out.println("Ingrese 0 para volver al menu principal");
        System.out.println("Elija el juego a gestionar:");
        int i = 1;
        for(Juego juego : JuegosController.getInstance().getJuegos()){
            System.out.println(i + ". " + juego.getTitulo());
            i++;
        }
    }

    @Override
    boolean ejecutarOperacionMenuPrincipal(Integer opcion) throws Exception {
        if (opcion == 0)
            return true;
        if(opcion > JuegosController.getInstance().getJuegos().size() || opcion < 0)
            throw new Exception("Opcion invalida");
        Juego juegoElegido = JuegosController.getInstance().getJuegos().get(opcion-1);
        new MenuGestionarJuego(juegoElegido).abrirMenu();
        return false;
    }
}
