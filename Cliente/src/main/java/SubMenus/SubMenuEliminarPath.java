package SubMenus;

import ApiClients.JuegoClient;
import Archivos.Directorio;
import Juegos.Juego;
import JuegosDtos.JuegoDTO;
import ServerManagment.ServerManager;

import java.util.Scanner;

public class SubMenuEliminarPath {
    JuegoDTO juego;
    public SubMenuEliminarPath(JuegoDTO juego){
        this.juego = juego;
    }
    public void abrirMenu() throws Exception {
        if(juego.getSaveFilePaths().isEmpty())
            throw  new Exception("Error: no hay paths guardados");
        int i = 0;
        System.out.println("Elija cual eliminar. Presione 0 para cancelar");
        for(Directorio path : juego.getSaveFilePaths()){
            System.out.println(i+1 + ". " + path.getPathPrincipal());
            i++;
        }
        int eliminado = new Scanner(System.in).nextInt() - 1;
        if(eliminado == -1){
            return;
        }
        if (eliminado > i || eliminado < 0)
            throw new Exception("Error: path invalido");
        System.out.println("Path \" " + juego.getSaveFilePaths().get(eliminado)+ "\" eliminado");
        juego.eliminarDirectorio(juego.getSaveFilePaths().get(i));
        new JuegoClient().patchearJuego(ServerManager.getInstance().getServidorLocal(), juego.getTitulo(), juego);
    }
}
