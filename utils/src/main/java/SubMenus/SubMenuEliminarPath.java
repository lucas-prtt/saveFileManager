package SubMenus;

import Juegos.Juego;

import java.util.Scanner;

public class SubMenuEliminarPath {
    Juego juego;
    public SubMenuEliminarPath(Juego juego){
        this.juego = juego;
    }
    public void abrirMenu() throws Exception {
        if(juego.getSaveFilePaths().isEmpty())
            throw  new Exception("Error: no hay paths guardados");
        int i = 0;
        System.out.println("Elija cual eliminar. Presione 0 para cancelar");
        for(String path : juego.getSaveFilePaths()){
            System.out.println(i+1 + ". " + path);
            i++;
        }
        int eliminado = new Scanner(System.in).nextInt() - 1;
        if(eliminado == -1){
            return;
        }
        if (eliminado > i || eliminado < 0)
            throw new Exception("Error: path invalido");
        System.out.println("Path \" " + juego.getSaveFilePaths().get(eliminado)+ "\" eliminado");
        juego.removeSaveFilePath(eliminado);
    }
}
