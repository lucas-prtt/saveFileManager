package SubMenus;

import Juegos.Juego;
import Juegos.Partida;
import Menus.MenuGestionarPartida;

import java.util.Scanner;

public class SubMenuEliminarPartida {
    Juego juego;
    public SubMenuEliminarPartida(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        System.out.println("Elija la partida para eliminar (Ingrese 0 para cancelar)");
        int i = 1;
        for(String tituloPartida : juego.getTitulosPartidas()){
            System.out.println(i+". "+tituloPartida);
            i++;
        }
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1)
            return;
        System.out.println("Eliminando la partida <"+juego.getPartidas().get(indice).getTitulo()+">");
        if(juego.getPartidaActual() == juego.getPartidas().get(indice)){
            juego.setPartidaActual(null);
            System.out.println("Removida de partida actual");
        }
        juego.eliminarPartida(indice);
        return;
    }
}
