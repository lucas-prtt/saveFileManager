package Juegos;


import java.util.ArrayList;
import java.util.List;

public class Juego {
    String titulo;
    List<Partida> partidas;
    Partida partidaActual;
    List<String> saveFilePaths;

    public Juego(String titulo){
        this.titulo = titulo;
        this.partidas = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>();
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
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
    public void setPartidaActual(Partida partidaActual){
        this.partidaActual = partidaActual;
    }
}
