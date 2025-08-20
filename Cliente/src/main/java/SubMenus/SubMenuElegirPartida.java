package SubMenus;

import Juegos.Juego;
import Juegos.Partida;
import Menus.MenuGestionarPartida;

import java.util.Scanner;

public class SubMenuElegirPartida {
    Juego juego;
    public SubMenuElegirPartida(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu(){
        System.out.println("Elija la partida");
        int i = 1;
        for(String tituloPartida : juego.getTitulosPartidas()){
            System.out.println(i+". "+tituloPartida);
            i++;
        }
        int indice = new Scanner(System.in).nextInt()-1;
        Partida partida = juego.getPartidas().get(indice);
        new MenuGestionarPartida(partida, juego).abrirMenu();

    }
}
