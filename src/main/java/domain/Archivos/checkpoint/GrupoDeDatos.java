package domain.Archivos.checkpoint;

import domain.Archivos.juego.Directorio;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class GrupoDeDatos {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable
    private List<Archivo> archivos;
    @ManyToOne
    @JoinColumn(name = "directorio_id")
    private Directorio directorio;
    public GrupoDeDatos(List<Archivo> archivos, Directorio directorio){
        this.archivos = archivos;
        this.directorio = directorio;
    }
    @Override
    public String toString(){
        return "(GrupoDeDatos - " + archivos + " )";
    }
}
