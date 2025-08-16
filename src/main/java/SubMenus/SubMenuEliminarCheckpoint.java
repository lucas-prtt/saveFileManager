package SubMenus;

import Juegos.Checkpoint;
import Juegos.Partida;

import java.util.Scanner;

public class SubMenuEliminarCheckpoint {
    Partida partida;

    public SubMenuEliminarCheckpoint(Partida partida) {
        this.partida = partida;
    }

    public void abrirMenu() throws Exception {
        System.out.println("Partidas actuales: ");
        int i = 1;
        for (Checkpoint checkpoint : partida.getCheckpoints()) {
            System.out.println(i + ". " + checkpoint.getStringReferencia());
            i++;
        }
        System.out.println("Elija la partida a eliminar, o presione 0 para cancelar");
        int indice = new Scanner(System.in).nextInt()-1;
        if(indice == -1){
            return;
        }
        else if (indice <0 || indice>=partida.getCheckpoints().size()){
            partida.eliminarCheckpoint(indice);
            return;
        }
        else throw new Exception("Opcion invalida para eliminar checkpoint");

    }
}
