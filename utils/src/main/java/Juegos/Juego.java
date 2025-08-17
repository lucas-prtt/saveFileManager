package Juegos;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor
public class Juego {
    @Setter
    @Id
    String titulo;
    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Partida> partidas;

    @Setter
            @OneToOne
    Partida partidaActual;
    @ElementCollection
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

    public void eliminarPartidaByIndex(int index){
        partidas.remove(index);
    }
    public void agregarPartida(Partida partida){
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
    public JuegoPatchDTO toDTO(){
        return new JuegoPatchDTO(titulo, saveFilePaths, partidaActual.getTitulo());
    }
    public void patchWithDto(JuegoPatchDTO juegoDto){
        if(juegoDto.titulo != null)
            this.titulo = juegoDto.titulo;
        if(juegoDto.saveFilePaths != null)
            this.saveFilePaths =  juegoDto.saveFilePaths;
        try {
            if (juegoDto.partidaActual != null)
                this.partidaActual = this.getPartidaByTitulo(juegoDto.partidaActual).orElseThrow();
        }
        catch (Exception e){
            System.out.println("Partida no encontrada");
        }
    }

}
