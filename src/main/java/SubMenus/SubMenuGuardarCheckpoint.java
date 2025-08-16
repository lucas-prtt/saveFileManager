package SubMenus;

import Juegos.Juego;
import Juegos.Partida;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuGuardarCheckpoint {
    Partida partida;
    Juego juego;

    public SubMenuGuardarCheckpoint(Partida partida, Juego juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {

        if (partida == juego.getPartidaActual()) {
            System.out.println("Ingrese el nombre del checkpoint (opcional)");
            String nombre = new Scanner(System.in).nextLine();
            if(Objects.equals(nombre, ""))
                partida.crearCheckpoint();
            else
                partida.crearCheckpoint(nombre);
        }else{
            System.out.println("Atención! La partida seleccionada es <"+partida.getTitulo()+">, la partida actualmente cargada es <"+juego.getPartidaActual().getTitulo()+">\nEsta seguro que desea guardar la partida actual aquí?" );
            System.out.println("1. Si\n2. No");
            int r = 0;
            while (r != 1 && r != 2) {
                r = new Scanner(System.in).nextInt();
            }
            if(r == 1){
                System.out.println("Ingrese el nombre del checkpoint (opcional)");
                String nombre = new Scanner(System.in).nextLine();
                if(Objects.equals(nombre, ""))
                    partida.crearCheckpoint();
                else
                    partida.crearCheckpoint(nombre);
                juego.setPartidaActual(partida);
                System.out.println("La partida actual se ha actualizado a la seleccionada ("+partida.getTitulo()+")");
            }
        }
    }
}
