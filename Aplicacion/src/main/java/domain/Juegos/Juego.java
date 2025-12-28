package domain.Juegos;


import domain.Archivos.Directorio;
import domain.Exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Entity
@AllArgsConstructor
public class Juego {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Setter
    String titulo;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Partida> partidas;
    @Setter
    @OneToOne
    @JsonIgnore
    Partida partidaActual;
    @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "juego_titulo")
    List<Directorio> saveFilePaths;

    public Juego(String titulo){
        this.titulo = titulo;
        this.partidas = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>();
    }
    public Juego(String titulo, List<Directorio> directorios){
        this.titulo = titulo;
        this.partidas = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>(directorios);
    }

    public Juego(){
        this.partidas = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>();
    }

    @JsonIgnore
    public List<String> getTitulosPartidas() {
        return partidas.stream().map(Partida::getTitulo).toList();
    }

    public void addSaveFilePath(Path path){
        saveFilePaths.add(new Directorio(path));
    }
    public void removeSaveFilePath(int index){
        saveFilePaths.remove(index);
    }

    public void eliminarPartidaByIndex(int index){
        partidas.remove(index);
    }
    public void eliminarPartidaByTitulo(String titulo){
        Optional<Partida> partidaAEliminar = partidas.stream().filter(partida -> Objects.equals(partida.titulo, titulo)).findFirst();
        if (partidaAEliminar.isEmpty()){
            throw new ResourceNotFoundException("No esta la partida que se quiere eliminar");
        }
        if(partidaAEliminar.get() == partidaActual)
            partidaActual = null;
        partidas.remove(partidaAEliminar.get());

    }
    public void agregarPartida(Partida partida){
        partida.setJuego(this);
        partidas.add(partida);
    }
    public void vaciarArchivosDeGuardado(){
        //TODO
    }
    public void eliminarPartida(Partida partida){
        partidas.remove(partida);
    }
    public Optional<Partida> getPartidaByTitulo(String titulo){
        return partidas.stream().filter(partida -> {return Objects.equals(partida.getTitulo(), titulo);}).findFirst();
    }

    public void eliminarDirectorio(Directorio directorio){
        saveFilePaths.remove(directorio);
    }

    public void agregarDirectorio(Directorio directorio){
        saveFilePaths.add(directorio);
    }

}
