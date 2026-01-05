package domain.Archivos.juego;

import domain.Archivos.PathConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Directorio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Convert(converter = PathConverter.class)
    Path pathPrincipal;

    public Directorio(Path pathPrincipal){
        this.pathPrincipal = pathPrincipal;
    }

    @Override
    public String toString(){
        return "(Directorio: " + id +" - " + pathPrincipal  + ") ";
    }
}
