package domain.Archivos.checkpoint;

import domain.Archivos.juego.Directorio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true) // Si se carga la lista de grupos de datos lazy del checkpoint tambien se cargan los archivos
    @JoinTable
    private List<Archivo> archivos;
    @ManyToOne
    @JoinColumn(name = "directorio_id")
    private Directorio directorio;
    public GrupoDeDatos(List<Archivo> archivos, Directorio directorio){
        this.archivos = archivos;
        this.directorio = directorio;
    }

}
