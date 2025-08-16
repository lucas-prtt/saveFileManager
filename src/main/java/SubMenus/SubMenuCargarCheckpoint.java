package SubMenus;

import Juegos.Checkpoint;
import Juegos.Juego;
import Juegos.Partida;

import java.util.Objects;
import java.util.Scanner;

public class SubMenuCargarCheckpoint {
    Partida partida;
    Juego juego;

    public SubMenuCargarCheckpoint(Partida partida, Juego juego) {
        this.partida = partida;
        this.juego = juego;
    }

    public void abrirMenu() throws Exception {
        System.out.println("Partidas actuales: ");
        int i = 1;
        for(Checkpoint checkpoint : partida.getCheckpoints()){
            System.out.println(i+". "+checkpoint.getStringReferencia());
            i++;
        }

        System.out.println("Elija la partida a cargar, o presione 0 para cancelar");
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1){
            return;
        }
        else if (indice <0 || indice>=partida.getCheckpoints().size()){
            System.out.println("Atención! Desea guardar el estado actual de la partida antes de cargar la nueva?" );
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
            }
            partida.cargarCheckpoint(partida.getCheckpoints().get(indice));
            return;
        }
        else throw new Exception("Opcion invalida para eliminar checkpoint");
    }
}
