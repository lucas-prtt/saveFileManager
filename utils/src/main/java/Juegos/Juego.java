package Juegos;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Entity
public class Juego {
    @Setter
    @Id
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

    public void addSaveFilePath(String path){
        saveFilePaths.add(path);
    }
    public void removeSaveFilePath(int index){
        saveFilePaths.remove(index);
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
    public Optional<Partida> getPartidaByTitulo(String titulo){
        return partidas.stream().filter(partida -> {return Objects.equals(partida.getTitulo(), titulo);}).findFirst();
    }
}
