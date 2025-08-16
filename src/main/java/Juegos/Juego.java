package Juegos;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Juego {
    @Getter @Setter
    String titulo;

    List<Partida> partidas;

    @Setter
    Partida partidaActual;

    List<String> saveFilePaths;

    public Juego(String titulo){
        this.titulo = titulo;
        this.partidas = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>();
    }

    public List<String> getTitulosPartidas() {
        return partidas.stream().map(Partida::getTitulo).toList();
    }
    public List<String> getSaveFilePaths() {
        return saveFilePaths;
    }
    public void addSaveFilePath(String path){
        saveFilePaths.add(path);
    }
    public void removeSaveFilePath(int index){
        saveFilePaths.remove(index);
    }
    public List<Partida>getPartidas(){
        return partidas;
    }
    public Partida getPartidaActual() {
        return partidaActual;
    }

    public void eliminarPartida(int index){
        partidas.remove(index);
    }
    public void agregarPartida(Partida partida){
        partidas.add(partida);
    }
    public void vaciarArchivosDeGuardado(){
        //TODO
    }
}
